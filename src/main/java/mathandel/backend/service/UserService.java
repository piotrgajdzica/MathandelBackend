package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.EditMeRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.payload.response.GetMeResponse;
import mathandel.backend.payload.response.GetUserResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {

    private final EditionRepository editionRepository;
    private final UserRepository userRepository;

    public UserService(EditionRepository editionRepository, UserRepository userRepository) {
        this.editionRepository = editionRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse joinEdition(Long userId, Long editionId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist."));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));

        user.getEditions().add(edition);
        userRepository.save(user);

        return new ApiResponse(true, "User added to edition successfully");
    }

    public GetUserResponse getUser(String userID) {
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(userID));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new GetUserResponse()
                    .setName(user.getName())
                    .setSurname(user.getSurname())
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
                    .setRoles(user.getRoles());
        } else {
            throw new ResourceNotFoundException("User", "id", userID);
        }
    }

    public GetMeResponse getMe(Long userID) {
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new GetMeResponse()
                    .setName(user.getName())
                    .setSurname(user.getSurname())
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
                    .setRoles(user.getRoles())
                    .setEditions(user.getEditions());
        } else {
            throw new ResourceNotFoundException("User", "id", userID);
        }
    }

    public ApiResponse editMe(Long userID, EditMeRequest editMeRequest) {

        Optional<User> optionalUser = userRepository.findById(userID);

        if (userRepository.existsByUsername(editMeRequest.getUsername())) {
            return new ApiResponse(false, "Username is already taken!");
        }

        if (userRepository.existsByEmail(editMeRequest.getEmail())) {
            return new ApiResponse(false, "Email Address already in use!");
        }

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setName(editMeRequest.getName());
            user.setSurname(editMeRequest.getSurname());
            user.setUsername(editMeRequest.getUsername());
            user.setEmail(editMeRequest.getEmail());

            userRepository.save(user);
            return new ApiResponse(true, "Successfully edited user");
        }

        return new ApiResponse(false, "User doesn't exist in database");
    }
}
