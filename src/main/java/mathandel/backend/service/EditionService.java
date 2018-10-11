package mathandel.backend.service;

import mathandel.backend.client.model.EditionTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.Role;
import mathandel.backend.model.User;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.model.enums.RoleName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//todo it tests
@Service
public class EditionService {

    private final EditionRepository editionRepository;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private final UserRepository userRepository;

    public EditionService(EditionRepository editionRepository, EditionStatusTypeRepository editionStatusTypeRepository, UserRepository userRepository) {
        this.editionRepository = editionRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse createEdition(EditionTO editionTO, Long userId) {

        if (editionRepository.existsByName(editionTO.getName())) {
            throw new BadRequestException("Edition name already exists");
        }
        if (editionTO.getEndDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Edition end date cannot be in the past");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        Set<User> moderators = new HashSet<>();
        Set<User> participants = new HashSet<>();
        moderators.add(user);
        participants.add(user);

        EditionStatusType editionStatusType = editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.OPENED);
        Edition edition = new Edition()
                .setName(editionTO.getName())
                .setEndDate(editionTO.getEndDate())
                .setModerators(moderators)
                .setParticipants(participants)
                .setEditionStatusType(editionStatusType)
                .setDescription(editionTO.getDescription())
                .setMaxParticipants(editionTO.getMaxParticipants());

        editionRepository.save(edition);

        return new ApiResponse(true, "Edition added successfully");
    }

    public ApiResponse editEdition(EditionTO editionTO, Long editionId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (!edition.getModerators().contains(user)) {
            throw new BadRequestException("You are not moderator of this edition");
        }
        if (editionRepository.existsByName(editionTO.getName())) {
            throw new BadRequestException("Edition name already exists");
        }
        if (editionTO.getEndDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Edition end date cannot be in the past");
        }
        if (editionTO.getMaxParticipants() < edition.getParticipants().size()) {
            throw new BadRequestException("Cannot lower max number of participants");
        }

        edition.setName(editionTO.getName());
        edition.setEndDate(editionTO.getEndDate());
        editionRepository.save(edition);

        return new ApiResponse(true, "Edition edited successfully");
    }

    public List<EditionTO> getEditions() {
        return mapEditions(editionRepository.findAll());
    }

    private List<EditionTO> mapEditions(List<Edition> all) {
        return all.stream().map(e -> new EditionTO()
                .setId(e.getId())
                .setName(e.getName())
                .setDescription(e.getDescription())
                .setEndDate(e.getEndDate())
                .setNumberOfParticipants(e.getParticipants().size())
                .setMaxParticipants(e.getMaxParticipants())).collect(Collectors.toList());
    }

    public ApiResponse makeUserEditionModerator(Long userId, Long editionId, String username) {
        User moderator = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        User requestedUser =  userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if(!edition.getModerators().contains(moderator)) {
            throw new BadRequestException("You have no access to this resource");
        }
        if(!requestedUser.getRoles().contains(new Role().setName(RoleName.ROLE_MODERATOR))) {
            throw new BadRequestException("Requested user is not moderator");
        }

        edition.getModerators().add(requestedUser);
        return new ApiResponse(true, "User " + username + " become moderator of edition " + editionId);
    }
}
