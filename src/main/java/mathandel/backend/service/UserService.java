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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final EditionRepository editionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(EditionRepository editionRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.editionRepository = editionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse joinEdition(Long userId, Long editionId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        user.getEditions().add(edition);
        userRepository.save(user);

        return new ApiResponse(true, "User added to edition successfully");
    }

    public GetUserResponse getUser(Long userID) {
        Optional<User> optionalUser = userRepository.findById(userID);
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

    //todo not tested
    public ApiResponse editMe(Long userId, EditMeRequest editMeRequest) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (userRepository.existsByUsername(editMeRequest.getUsername())) {
            return new ApiResponse(false, "Username is already taken");
        }

        if (userRepository.existsByEmail(editMeRequest.getEmail())) {
            return new ApiResponse(false, "Email Address already in use");
        }

        if (optionalUser.isPresent()) {
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

    //todo not tested
    public ApiResponse changePassword(Long userId, String newPassword) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return new ApiResponse(true, "Password changed successfully");
        }
        return new ApiResponse(false, "User doesn't exist in database");
    }
}
