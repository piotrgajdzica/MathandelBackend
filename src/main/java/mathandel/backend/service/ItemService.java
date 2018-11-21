package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ItemTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.Item;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ItemRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapItem;
import static mathandel.backend.utils.ServerToClientDataConverter.mapItems;

//todo it tests
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository, EditionRepository editionRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
    }

    public ItemTO createItem(Long userId, Long editionId, ItemTO itemTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        validateEditionAndUser(user, edition);

        Item item = new Item()
                .setName(itemTO.getName())
                .setDescription(itemTO.getDescription())
                .setUser(user)
                .setEdition(edition);

        return mapItem(itemRepository.save(item));
    }

    //todo if preferences exists should not be able to do this

    public ItemTO editItem(Long userId, ItemTO itemTO, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if (!user.getId().equals(item.getUser().getId())) {
            throw new BadRequestException("You have no access to this resource");
        }

        Edition edition = item.getEdition();
        if (edition != null) {
            EditionStatusType editionStatusType = edition.getEditionStatusType();
            if (editionStatusType.getEditionStatusName() != EditionStatusName.OPENED) {
                throw new BadRequestException("Item's edition is not opened");
            }
        }

        item.setDescription(itemTO.getDescription())
                .setName(itemTO.getName());

        return mapItem(itemRepository.save(item));
    }
    public ItemTO getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        return mapItem(item);
    }

    public ItemTO assignItemToEdition(Long userId, Long editionId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        validateEditionAndUser(user, edition);
        if (!userId.equals(item.getUser().getId())) {
            throw new BadRequestException("You have no access to this item");
        }
        if (item.getEdition() != null) {
            throw new BadRequestException("Item already in edition");
        }

        item.setEdition(edition);
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

    static void validateEditionAndUser(User user, Edition edition) {
        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
    }

    private void validateGetItems(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User not in this edition");
        }
    }
}
