package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.UserTO;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static mathandel.backend.utils.ServerToClientDataConverter.mapUser;

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

    @Transactional
    public ApiResponse joinEdition(Long userId, Long editionId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        if (!edition.getEditionStatusType().getEditionStatusName().equals(EditionStatusName.OPENED)) {
            throw new BadRequestException("Edition " + edition.getName() + " is not opened");
        }
        if (edition.getParticipants().size() == edition.getMaxParticipants()) {
            throw new BadRequestException("Edition is full");
        }
        if (edition.getParticipants().contains(user)) {
            throw new BadRequestException("User already in this edition");
        }

        edition.getParticipants().add(user);
        editionRepository.save(edition);

        return new ApiResponse("User added to edition successfully");
    }

    public UserTO getUserData(Long userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User", "id", userID));
        return mapUser(user);
    }

    public UserTO editMyData(Long userId, UserTO userTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));

        if (!user.getUsername().equals(userTO.getUsername()) && userRepository.existsByUsername(userTO.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (!user.getEmail().equals(userTO.getEmail()) && userRepository.existsByEmail(userTO.getEmail())) {
            throw new BadRequestException("Email Address already in use");
        }

        user.setName(userTO.getName())
                .setSurname(userTO.getSurname())
                .setUsername(userTO.getUsername())
                .setEmail(userTO.getEmail())
                .setAddress(userTO.getAddress());

        return mapUser(userRepository.save(user));
    }

    public ApiResponse changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse("Password changed successfully");
    }
}
