package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.DefinedGroupContentTO;
import mathandel.backend.model.client.DefinedGroupTO;
import mathandel.backend.model.server.DefinedGroup;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.Item;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.DefinedGroupRepository;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ItemRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapDefinedGroup;
import static mathandel.backend.utils.ServerToClientDataConverter.mapDefinedGroups;


@Service
public class DefinedGroupService {

    private final UserRepository userRepository;
    private final EditionRepository editionRepository;
    private final DefinedGroupRepository definedGroupRepository;
    private final ItemRepository itemRepository;

    public DefinedGroupService(UserRepository userRepository, EditionRepository editionRepository, DefinedGroupRepository definedGroupRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.itemRepository = itemRepository;
    }

    public DefinedGroupTO createDefinedGroup(Long userId, Long editionId, DefinedGroupTO definedGroupTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        String groupName = definedGroupTO.getName();

        if(!edition.getEditionStatusType().getEditionStatusName().equals(EditionStatusName.OPENED)) {
            throw new BadRequestException("Edition " + edition.getName() + " is not opened");
        }
        if(definedGroupRepository.existsByNameAndUser_IdAndEdition_Id(groupName, userId, editionId)) {
            throw new BadRequestException("You already have a definedGroup named '" + groupName + "' in edition " + edition.getName());
        }
        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User not in this edition");
        }

        DefinedGroup definedGroup = new DefinedGroup()
                .setName(groupName)
                .setUser(user)
                .setEdition(edition);

        return mapDefinedGroup(definedGroupRepository.save(definedGroup));
    }

    public DefinedGroupTO editDefinedGroup(Long userId, Long editionId, Long groupId, DefinedGroupTO definedGroupTO) {
        DefinedGroup definedGroup = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));
        validateGroup(definedGroup, editionId, userId, groupId);

        definedGroup.setName(definedGroupTO.getName());
        return mapDefinedGroup(definedGroupRepository.save(definedGroup));
    }

    public Set<DefinedGroupTO> getDefinedGroupsFromEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if(!edition.getEditionStatusType().getEditionStatusName().equals(EditionStatusName.OPENED)) {
            throw new BadRequestException("Edition " + edition.getName() + " is not opened");
        }
        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }

        Set<DefinedGroup> definedGroups = definedGroupRepository.findByUser_IdAndEdition_Id(userId, editionId);
        return mapDefinedGroups(definedGroups);
    }

    public DefinedGroupTO updateDefinedGroup(Long userId, Long editionId, Long groupId, DefinedGroupContentTO definedGroupContentTO) {
        DefinedGroup group = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));

        validateGroup(group, editionId, userId, groupId);

        Set<Item> items = new HashSet<>();
        Set<DefinedGroup> groups = new HashSet<>();

        for(Long itemId: definedGroupContentTO.getItemsIds()) {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

            if(item.getUser().getId().equals(userId)){
                throw new BadRequestException("Item belongs to user " + userId);
            }
            if(!item.getEdition().getId().equals(editionId)) {
                throw new BadRequestException("Item does not belong to edition " + editionId);
            }
            items.add(item);
        }

        for(Long definedGroupId: definedGroupContentTO.getGroupIds()) {
            DefinedGroup definedGroup = definedGroupRepository.findById(definedGroupId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", definedGroupId));

            if(!definedGroup.getUser().getId().equals(userId)){
                throw new BadRequestException("Group does not belong to user " + userId);
            }
            if(!definedGroup.getEdition().getId().equals(editionId)) {
                throw new BadRequestException("Group does not belong to edition " + editionId);
            }
            groups.add(definedGroup);
        }

        group.setItems(items);
        group.setGroups(groups);

        return mapDefinedGroup(definedGroupRepository.save(group));
    }

    private void validateGroup(DefinedGroup group, Long editionId, Long userId, Long groupId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if(!edition.getEditionStatusType().getEditionStatusName().equals(EditionStatusName.OPENED)) {
            throw new BadRequestException("Edition " + edition.getName() + " is not opened");
        }
        if(!group.getEdition().getId().equals(editionId)) {
            throw new BadRequestException("Group " + groupId + " is not in edition " + editionId);
        }
        if(!group.getUser().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not creator of group " + groupId);
        }
    }

    public DefinedGroupTO getDefinedGroup(Long userId, Long editionID, Long groupId) {
        DefinedGroup definedGroup = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));

        if(!definedGroup.getUser().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not creator of group " + groupId);
        }
        return mapDefinedGroup(definedGroup);
    }
}
