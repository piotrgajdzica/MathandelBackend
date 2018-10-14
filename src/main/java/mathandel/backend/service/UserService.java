package mathandel.backend.service;

import mathandel.backend.client.model.RoleTO;
import mathandel.backend.client.model.UserTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.Role;
import mathandel.backend.model.User;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

//todo it tests
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
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        if (edition.getParticipants().size() == edition.getMaxParticipants()) {
            throw new BadRequestException("Edition is full");
        }

        edition.getParticipants().add(user);
        editionRepository.save(edition);

        return new ApiResponse("User added to edition successfully");
    }

    public UserTO getUserData(Long userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));
        return mapUser(user);
    }

    public ApiResponse editMyData(Long userId, UserTO userTO) {

        if (userRepository.existsByUsername(userTO.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(userTO.getEmail())) {
            throw new BadRequestException("Email Address already in use");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"))
                .setName(userTO.getName())
                .setSurname(userTO.getSurname())
                .setUsername(userTO.getUsername())
                .setEmail(userTO.getEmail());

        userRepository.save(user);
        return new ApiResponse("Successfully edited user");
    }

    public ApiResponse changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse("Password changed successfully");
    }

    public static UserTO mapUser(User user) {
        return new UserTO()
                .setId(user.getId())
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRoles(mapRoles(user.getRoles()));
    }

    public static Set<RoleTO> mapRoles(Set<Role> roles) {
        return roles.stream().map(r -> new RoleTO().setRoleName(r.getName())).collect(Collectors.toSet());
    }
}
