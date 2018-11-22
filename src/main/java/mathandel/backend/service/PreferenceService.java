package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.server.*;
import mathandel.backend.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapPreference;
import static mathandel.backend.utils.ServerToClientDataConverter.mapPreferences;

@Service
public class PreferenceService {

    private ItemRepository itemRepository;
    private PreferenceRepository preferenceRepository;
    private EditionRepository editionRepository;
    private UserRepository userRepository;
    private DefinedGroupRepository definedGroupRepository;

    public PreferenceService(ItemRepository itemRepository, PreferenceRepository preferenceRepository, EditionRepository editionRepository, UserRepository userRepository, DefinedGroupRepository definedGroupRepository) {
        this.itemRepository = itemRepository;
        this.preferenceRepository = preferenceRepository;
        this.editionRepository = editionRepository;
        this.userRepository = userRepository;
        this.definedGroupRepository = definedGroupRepository;
    }

    @Transactional
    public PreferenceTO updatePreference(Long userId, PreferenceTO preferenceTO, Long editionId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        Item haveItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if(!haveItem.getEdition().getId().equals(editionId)){
            throw new BadRequestException("Item not in this edition");
        }
        ItemService.validateEditionAndUser(user, edition);
        if (!haveItem.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not an owner of this item");
        }

        Optional<Preference> optionalPreference = preferenceRepository.findByHaveItem_Id(itemId);
        Preference preference = optionalPreference.orElseGet(() -> new Preference()
                .setHaveItem(haveItem)
                .setUser(user)
                .setEdition(edition));

        Set<Item> items = new HashSet<>(itemRepository.findAllById(preferenceTO.getWantedItemsIds()));
        Set<DefinedGroup> groups = new HashSet<>(definedGroupRepository.findAllById(preferenceTO.getWantedDefinedGroupsIds()));

        for (Item item : items) {
            if (item.getUser().getId().equals(userId)) {
                throw new BadRequestException("User is the owner of item " + item.getId());
            }
        }

        for (DefinedGroup definedGroup : groups) {
            if (!definedGroup.getUser().getId().equals(userId)) {
                throw new BadRequestException("User is not the owner of group " + definedGroup.getId());
            }
        }

        preference.setWantedItems(items);
        preference.setWantedDefinedGroups(groups);

        return mapPreference(preferenceRepository.save(preference));
    }

    public Set<PreferenceTO> getUserPreferencesFromOneEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        ItemService.validateEditionAndUser(user, edition);

        return mapPreferences(preferenceRepository.findAllByUser_IdAndEdition_Id(userId, editionId));
    }

    public PreferenceTO getPreferenceForItem(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if (!item.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not allowed to know other's user items preferences");
        }

        Preference preference = preferenceRepository.findByHaveItem_Id(itemId).orElseThrow(
                () -> new ResourceNotFoundException("Preference", "itemId", itemId));

        return mapPreference(preference);
    }
}
