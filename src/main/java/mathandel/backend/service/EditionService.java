package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatus;
import mathandel.backend.model.EditionStatusName;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.AddEditEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EditionService {

    @Autowired
    EditionRepository editionRepository;

    @Autowired
    EditionStatusRepository editionStatusRepository;

    @Autowired
    UserRepository userRepository;


    public ApiResponse addEdition(AddEditEditionRequest addEditEditionRequest, Long userId){

        if (editionRepository.existsByName(addEditEditionRequest.getName())) {
            return new ApiResponse(false, "Edition name already exists.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Set<User> moderators = new HashSet<>();
        moderators.add(user);

        EditionStatus editionStatus = editionStatusRepository.findByEditionStatusName(EditionStatusName.OPENED);
        Edition edition = new Edition(addEditEditionRequest.getName(), addEditEditionRequest.getEndDate(), moderators, editionStatus);
        user.getEditions().add(edition);

        editionRepository.save(edition);
        userRepository.save(user);

        return new ApiResponse(true, "Edition added successfully");
    }

    public ApiResponse editEdition(AddEditEditionRequest addEditEditionRequest, Long editionId, Long userId){

        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist."));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));

        if (!edition.getModerators().contains(user)){
            return new ApiResponse(false, "You are not moderator of this edition.");
        }
        if (editionRepository.existsByName(addEditEditionRequest.getName())) {
            return new ApiResponse(false, "Edition name already exists.");
        }
        if(addEditEditionRequest.getEndDate().isAfter(LocalDate.now())){
            return new ApiResponse(false, "Edition end date cannot be in the past.");
        }

        edition.setName(addEditEditionRequest.getName());
        edition.setEndDate(addEditEditionRequest.getEndDate());
        editionRepository.save(edition);

        return new ApiResponse(true, "Edition edited successfully");
    }

    public List<Edition> getEditions(){
        return editionRepository.findAll();
    }

}
