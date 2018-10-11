package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.*;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.payload.request.ModeratorRequestReasonRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModeratorRequestsRepository moderatorRequestsRepository;

    @Autowired
    UserRepository userRepository;

    public ApiResponse requestModerator(@Valid ModeratorRequestReasonRequest reason, Long userId) {
        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isPresent() && hasRole(RoleName.ROLE_MODERATOR, optUser.get()))
            return new ApiResponse(false, "Role already given");

        Optional<ModeratorRequest> optModeeratorRequest = moderatorRequestsRepository.findByUser(optUser.get());

        if (optModeeratorRequest.isPresent())
            return new ApiResponse(false, "Request already submitted");

        ModeratorRequest moderatorRequest = new ModeratorRequest();

        ModeratorRequestStatus moderatorRequestStatus = new ModeratorRequestStatus();
        moderatorRequestStatus.setName(ModeratorRequestStatusName.PENDING);

        moderatorRequest.setUser(optUser.get())
                .setReason(reason.getReason())
                .setModeratorRequestStatus(moderatorRequestStatus);
        moderatorRequestsRepository.save(moderatorRequest);

        return new ApiResponse(true, "Request submitted");
    }

    public List<mathandel.backend.model.client.ModeratorRequestTO> getModeratorRequests() {
        return moderatorRequestsRepository.findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName.PENDING).stream().map(element -> ServerToClientDataConverter.mapModeratorRequest(element)).collect(Collectors.toList());
    }


    public ApiResponse resolveModeratorRequests(List<ModeratorRequestTO> moderatorRequestMessageRequests) {
        ModeratorRequest moderatorRequest;

        for (ModeratorRequestTO moderatorRequestMessageRequest : moderatorRequestMessageRequests) {
            moderatorRequest = moderatorRequestsRepository.findModeratorRequestsByUser_Id(moderatorRequestMessageRequest.getUserId()).orElseThrow(() -> new AppException("No entry in moderator_requests for user " + moderatorRequestMessageRequest.getUserId()));
            moderatorRequestsRepository.save(moderatorRequest.setModeratorRequestStatus(moderatorRequest.getModeratorRequestStatus().setName(ModeratorRequestStatusName.ACCEPTED)));
        }

        return new ApiResponse(true, "Requests resolved");
    }

    private boolean hasRole(RoleName roleName, User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public ModeratorRequest getUserRequests(Long userId) {
        return moderatorRequestsRepository.findModeratorRequestsByUser_Id(userId).orElseThrow(() -> new AppException("No entry in moderator_requests for user " + userId));
    }
}
