package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.User;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

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

}
