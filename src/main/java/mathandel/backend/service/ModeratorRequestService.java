package mathandel.backend.service;

import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.ModeratorRequestStatusRepository;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static mathandel.backend.model.server.enums.ModeratorRequestStatusName.ACCEPTED;
import static mathandel.backend.model.server.enums.ModeratorRequestStatusName.REJECTED;
import static mathandel.backend.utils.ServerToClientDataConverter.mapModeratorRequest;
import static mathandel.backend.utils.ServerToClientDataConverter.mapModeratorRequests;

@Service
public class ModeratorRequestService {

    private final ModeratorRequestsRepository moderatorRequestsRepository;
    private final UserRepository userRepository;
    private final ModeratorRequestStatusRepository moderatorRequestStatusRepository;
    private final RoleRepository roleRepository;

    public ModeratorRequestService(ModeratorRequestsRepository moderatorRequestsRepository, UserRepository userRepository, RoleRepository roleRepository, ModeratorRequestStatusRepository moderatorRequestStatusRepository, RoleRepository roleRepository1) {
        this.moderatorRequestsRepository = moderatorRequestsRepository;
        this.userRepository = userRepository;
        this.moderatorRequestStatusRepository = moderatorRequestStatusRepository;
        this.roleRepository = roleRepository1;
    }

    public ModeratorRequestTO requestModerator( ModeratorRequestTO moderatorRequestTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        ModeratorRequest moderatorRequest = moderatorRequestsRepository.findModeratorRequestsByUser_Id(userId).orElse(new ModeratorRequest());

        if(moderatorRequestsRepository.existsByUser(user) &&  ModeratorRequestStatusName.PENDING.equals(moderatorRequest.getModeratorRequestStatus().getName()) ) {
            throw new BadRequestException("Request already submitted and pending for acceptance/rejection");
        }

        ModeratorRequestStatus moderatorRequestStatus = moderatorRequestStatusRepository.findByName(ModeratorRequestStatusName.PENDING).orElseThrow(() -> new AppException("Cannot find moderator request status"));

        moderatorRequest.setUser(user)
                .setReason(moderatorRequestTO.getReason())
                .setModeratorRequestStatus(moderatorRequestStatus);

        return mapModeratorRequest(moderatorRequestsRepository.save(moderatorRequest));
    }

    public Set<ModeratorRequestTO> getPendingModeratorRequests() {
        return mapModeratorRequests(moderatorRequestsRepository.findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName.PENDING));
    }

    public ApiResponse resolveModeratorRequests(Set<ModeratorRequestTO> resolvedRequests) {
        ModeratorRequestStatus accepted = moderatorRequestStatusRepository.findByName(ACCEPTED).orElseThrow(() -> new AppException("Cannot find moderator request status"));
        ModeratorRequestStatus rejected = moderatorRequestStatusRepository.findByName(REJECTED).orElseThrow(() -> new AppException("Cannot find moderator request status"));

        for(ModeratorRequestTO resolvedRequest : resolvedRequests) {
            ModeratorRequest moderatorRequest = moderatorRequestsRepository.findById(resolvedRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("ModeratorRequest", "id", resolvedRequest.getId()));
            if(resolvedRequest.getModeratorRequestStatus().equals(ACCEPTED)) {
                User user = userRepository.findById(moderatorRequest.getUser().getId()).orElseThrow(() -> new AppException("User does not exist"));
                Role role = roleRepository.findByName(RoleName.ROLE_MODERATOR).orElseThrow(() -> new AppException("Moderator Role not set"));
                moderatorRequest.setModeratorRequestStatus(accepted);
                user.getRoles().add(role);
            }
            if(resolvedRequest.getModeratorRequestStatus().equals(REJECTED)) {
                moderatorRequest.setModeratorRequestStatus(rejected);
            }
            moderatorRequestsRepository.save(moderatorRequest);
        }
        return new ApiResponse("Requests resolved");
    }

    public ModeratorRequestTO getUserRequests(Long userId) {
        return mapModeratorRequest(moderatorRequestsRepository.findAllByUser_Id(userId)
                .orElseThrow(() -> new BadRequestException("User have no moderator request")));
    }
}
