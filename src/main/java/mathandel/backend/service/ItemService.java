package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ItemTO;
import mathandel.backend.model.client.request.CreateUpdateItemRequest;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ImageRepository;
import mathandel.backend.repository.ItemRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static mathandel.backend.utils.ImageTypeMap.getExtension;
import static mathandel.backend.utils.ServerToClientDataConverter.mapItem;
import static mathandel.backend.utils.ServerToClientDataConverter.mapItems;

//todo it tests
@Service
public class ItemService {

    private final ImageRepository imageRepository;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;
    @Value("${maximum-file-size}")
    private long maxSize;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository, EditionRepository editionRepository, ImageRepository imageRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
        this.imageRepository = imageRepository;
    }

    static void validateEdition(User user, Edition edition) {
        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
    }

    public ItemTO createItem(Long userId, Long editionId, CreateUpdateItemRequest createUpdateItemRequest, MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        validateEdition(user, edition);

        Item item = new Item()
                .setName(createUpdateItemRequest.getName())
                .setDescription(createUpdateItemRequest.getDescription())
                .setUser(user)
                .setEdition(edition);

        item = itemRepository.save(item);

        return saveItemWithImages(userId, image1, image2, image3, item);
    }

    private void processFile(Long userId, MultipartFile file, Item item) {
        String extension;
        String name;
        if (file != null) {
            extension = getExtension(file.getContentType());
            name = generateName(userId, item.getId()) + "." + extension;

            validateFile(file, extension, maxSize);

            if (file.getSize() > 0) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get("src/main/resources/images/" + name);
                    Files.write(path, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new AppException("Error occurred when trying to save file");
                }

                Image image = new Image()
                        .setName(name);

                item.getImages().add(image);
            }
        }
    }

    static void validateFile(MultipartFile multipartFile, String extension, long maxSize) {
        if (multipartFile.getSize() > maxSize) {
            throw new BadRequestException("File cannot exceed 5mb");
        }
        if (!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image/")) {
            throw new BadRequestException("You can only upload an image");
        }
        if (extension == null) {
            throw new BadRequestException("File type not supported");
        }
    }

    public ItemTO editItem(Long userId, Long itemId, CreateUpdateItemRequest createUpdateItemRequest, MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if (!user.getId().equals(item.getUser().getId())) {
            throw new BadRequestException("You have no access to this resource");
        }

        Edition edition = item.getEdition();
        validateEdition(user, edition);
        if (edition != null) {
            EditionStatusType editionStatusType = edition.getEditionStatusType();
            if (editionStatusType.getEditionStatusName() != EditionStatusName.OPENED) {
                throw new BadRequestException("Item's edition is not opened");
            }
        }

        item.setDescription(createUpdateItemRequest.getDescription())
                .setName(createUpdateItemRequest.getName());

        int imagesInItem = item.getImages().size();
        int imagesToRemove = createUpdateItemRequest.getImagesToRemove().size();
        int newImagesCounter = 0;
        //todo getSize
        if (image1 != null) newImagesCounter++;
        if (image2 != null) newImagesCounter++;
        if (image3 != null) newImagesCounter++;

        if (imagesInItem - imagesToRemove + newImagesCounter > 3) {
            throw new BadRequestException("Trying to have more than 3 photos in an item");
        }

        createUpdateItemRequest.getImagesToRemove().forEach(imageToRemoveId -> {
            Image image = imageRepository.findById(imageToRemoveId).orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageToRemoveId));

            if (item.getImages().stream().noneMatch(itemImage -> itemImage.getId().equals(imageToRemoveId))) {
                throw new BadRequestException("Image with id " + imageToRemoveId + " is not assigned to item " + itemId);
            }

            File file = new File("src/main/resources/images/" + image.getName());
            if (!file.delete()) {
                throw new AppException("Could not delete file - server internal error");
            }
            imageRepository.delete(image);
            item.getImages().remove(image);
        });

        return saveItemWithImages(userId, image1, image2, image3, item);
    }

    public ItemTO getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        return mapItem(item);
    }

    private ItemTO saveItemWithImages(Long userId, MultipartFile image1, MultipartFile image2, MultipartFile image3, Item item) {
        processFile(userId, image1, item);
        processFile(userId, image2, item);
        processFile(userId, image3, item);
        return mapItem(itemRepository.save(item));
    }

    public Set<ItemTO> getNotAssignedItems(Long userId) {
        Set<Item> items = itemRepository.findByUser_IdAndEditionIsNull(userId);
        return mapItems(items);
    }

    public Set<ItemTO> getItemsFromEdition(Long userId, Long editionId) {
        validateGetItems(userId, editionId);
        return mapItems(itemRepository.findByEdition_IdAndUser_IdNot(editionId, userId));
    }

    public Set<ItemTO> getMyItemsFromEdition(Long userId, Long editionId) {
        validateGetItems(userId, editionId);

        return mapItems(itemRepository.findByEdition_IdAndUser_Id(editionId, userId));
    }

    public ItemTO assignItemToEdition(Long userId, Long editionId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        validateEdition(user, edition);
        if (!userId.equals(item.getUser().getId())) {
            throw new BadRequestException("You have no access to this item");
        }
        if (item.getEdition() != null) {
            throw new BadRequestException("Item already in edition");
        }

        item.setEdition(edition);
        return mapItem(itemRepository.save(item));
    }

    private void validateGetItems(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User not in this edition");
        }
    }

    private String generateName(Long userId, Long itemId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String dateString = format.format(new Date());
        return userId.toString() + "_" + itemId + "_" + dateString;
    }
}
