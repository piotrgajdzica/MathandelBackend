package mathandel.backend.service;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.ModeratorRequestStatusName;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapModeratorRequests;

@Service
public class ModeratorRequestService {

    private final ModeratorRequestsRepository moderatorRequestsRepository;
    private final UserRepository userRepository;

    public ModeratorRequestService(ModeratorRequestsRepository moderatorRequestsRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.moderatorRequestsRepository = moderatorRequestsRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse requestModerator( ModeratorRequestTO moderatorRequestTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        ModeratorRequest moderatorRequest = moderatorRequestsRepository.findModeratorRequestsByUser_Id(userId).orElse(new ModeratorRequest());

        if(moderatorRequestsRepository.existsByUser(user) &&  ModeratorRequestStatusName.PENDING.equals(moderatorRequest.getModeratorRequestStatus().getName()) ) {
            throw new BadRequestException("Request already submitted and pending for acceptance/rejection");
        }

        moderatorRequest.setUser(user)
                .setReason(moderatorRequestTO.getReason())
                .setModeratorRequestStatus(new ModeratorRequestStatus().setName(ModeratorRequestStatusName.PENDING));

        moderatorRequestsRepository.save(moderatorRequest);

        return new ApiResponse("Request submitted");
    }

    public Set<ModeratorRequestTO> getPendingModeratorRequests() {
        return mapModeratorRequests(moderatorRequestsRepository.findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName.PENDING));
    }

    public ApiResponse resolveModeratorRequests(Set<ModeratorRequestTO> resolvedRequests) {
        ModeratorRequest moderatorRequest;

        for (ModeratorRequestTO resolvedRequest : resolvedRequests) {
            moderatorRequest = moderatorRequestsRepository.findById(resolvedRequest.getId())
                    .orElseThrow(() -> new AppException("No entry in moderator_requests for user " + resolvedRequest.getUserId()));

            moderatorRequest
                    .getModeratorRequestStatus()
                    .setName(resolvedRequest.getModeratorRequestStatus());

            moderatorRequestsRepository.save(moderatorRequest);
        }

        return new ApiResponse("Requests resolved");
    }

    public Set<ModeratorRequestTO> getUserRequests(Long userId) {
        Set<ModeratorRequest> moderatorRequests =
                moderatorRequestsRepository
                        .findAllByUser_Id(userId);

        return mapModeratorRequests(moderatorRequests);
    }
}
