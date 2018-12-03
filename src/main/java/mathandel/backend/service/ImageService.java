package mathandel.backend.service;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ImageTO;
import mathandel.backend.model.server.Image;
import mathandel.backend.model.server.Item;
import mathandel.backend.model.server.User;
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

import static mathandel.backend.utils.ImageTypeMap.getExtension;
import static mathandel.backend.utils.ServerToClientDataConverter.mapImage;

//todo review and refactor
@Service
public class ImageService {

    @Value("${maximum-file-size}")
    private long maxSize;

    @Value("${maximum-images-per-item}")
    private int maxImagesPerItem;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public ImageService(ItemRepository itemRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public ImageTO addImage(Long userId, Long itemId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        String basePath = new File("").getAbsolutePath();
        String extension = getExtension(file.getContentType());
        String name = generateName(userId, itemId) + "." + extension;

        if (!item.getUser().equals(user)) {
            throw new BadRequestException("You are not the owner of this item");
        }
        if (item.getImages().size() == maxImagesPerItem) {
            throw new BadRequestException("Item already has 3 images");
        }
        ItemService.validateFile(file, extension, maxSize);

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

        Image uploadedImage = imageRepository.save(image);
        return mapImage(uploadedImage);
    }

    private String generateName(Long userId, Long itemId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String dateString = format.format(new Date());
        return userId.toString() + "_" + itemId + "_" + dateString;
    }

    public byte[] getImage(String imageName) {
        try {
            return Files.readAllBytes(Paths.get("src/main/resources/images/" + imageName));
        } catch (IOException e) {
            throw new BadRequestException("Requested file does not exist");
        }
    }

    public ApiResponse deleteImage(Long userId, Long itemId, String imageName) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        Image image = imageRepository.findByName(imageName).orElseThrow(() -> new ResourceNotFoundException("Image", "name", imageName));

        if(!item.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not the owner of this item");
        }
        if(!item.getImages().contains(image)) {
            throw new BadRequestException("This image does not belong to your this item");
        }

        item.getImages().remove(image);
        imageRepository.delete(image);
        File file = new File("src/main/resources/images/" + imageName);
        if(!file.delete()) {
           throw new AppException("Could not delete file - server internal error");
        }
        return new ApiResponse("Image deleted successfully");
    }
}
