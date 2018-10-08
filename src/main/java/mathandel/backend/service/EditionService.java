package mathandel.backend.service;

import mathandel.backend.client.model.EditionTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.model.User;
import mathandel.backend.client.request.EditionDataRequest;
import mathandel.backend.client.response.ApiResponse;
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

    public ApiResponse createEdition(EditionDataRequest editionDataRequest, Long userId) {

        if (editionRepository.existsByName(editionDataRequest.getName())) {
            return new ApiResponse(false, "Edition name already exists.");
        }
        if (editionDataRequest.getEndDate().isBefore(LocalDate.now())) {
            return new ApiResponse(false, "Edition end date cannot be in the past");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        Set<User> moderators = new HashSet<>();
        Set<User> participants = new HashSet<>();
        moderators.add(user);
        participants.add(user);

        EditionStatusType editionStatusType = editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.OPENED);
        Edition edition = new Edition()
                .setName(editionDataRequest.getName())
                .setEndDate(editionDataRequest.getEndDate())
                .setModerators(moderators)
                .setParticipants(participants)
                .setEditionStatusType(editionStatusType)
                .setDescription(editionDataRequest.getDescription())
                .setMaxParticipants(editionDataRequest.getMaxParticipants());

        editionRepository.save(edition);

        return new ApiResponse(true, "Edition added successfully");
    }

    public ApiResponse editEdition(EditionDataRequest editionDataRequest, Long editionId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));
        Edition edition = editionRepository.findById(editionId).orElse(null);

        if(edition == null) {
            return new ApiResponse(false, "Edition doesn't exist");
        }
        if (!edition.getModerators().contains(user)) {
            return new ApiResponse(false, "You are not moderator of this edition");
        }
        if (editionRepository.existsByName(editionDataRequest.getName())) {
            return new ApiResponse(false, "Edition name already exists");
        }
        if (editionDataRequest.getEndDate().isBefore(LocalDate.now())) {
            return new ApiResponse(false, "Edition end date cannot be in the past");
        }
        if (editionDataRequest.getMaxParticipants() < edition.getParticipants().size()) {
            return new ApiResponse(false, "Cannot lower max number of participants");
        }

        edition.setName(editionDataRequest.getName());
        edition.setEndDate(editionDataRequest.getEndDate());
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

}
