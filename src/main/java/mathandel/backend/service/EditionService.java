package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.AddEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EditionService {

    @Autowired
    EditionRepository editionRepository;

    @Autowired
    UserRepository userRepository;


    public ApiResponse addEdition(AddEditionRequest addEditionRequest, Long userId){

        if (editionRepository.existsByName(addEditionRequest.getName())) {
            return new ApiResponse(false, "Edition name already exists!");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Set<User> moderators = new HashSet<>();
        moderators.add(user);

        Edition edition = new Edition(addEditionRequest.getName(), addEditionRequest.getEndDate(), moderators);
        user.getEditions().add(edition);

        editionRepository.save(edition);
        userRepository.save(user);

        return new ApiResponse(true, "Edition added successfully");
    }

    public List<Edition> getEditions(){

        return editionRepository.findAll();

    }

}
