package mathandel.backend.service;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.ModeratorRequestStatusName;
import mathandel.backend.model.server.User;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static mathandel.backend.utils.ServerToClientDataConverter.mapModeratorRequest;

@Service
public class RoleService {

    private final ModeratorRequestsRepository moderatorRequestsRepository;
    private final UserRepository userRepository;

    public RoleService(ModeratorRequestsRepository moderatorRequestsRepository, UserRepository userRepository) {
        this.moderatorRequestsRepository = moderatorRequestsRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse requestModerator( ModeratorRequestTO moderatorRequestTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        if(moderatorRequestsRepository.existsByUser(user)) {
            throw new BadRequestException("Request already submitted");
        }

        ModeratorRequest moderatorRequest = new ModeratorRequest().setUser(user)
                .setReason(moderatorRequestTO.getReason())
                .setModeratorRequestStatus(new ModeratorRequestStatus().setName(ModeratorRequestStatusName.PENDING));

        moderatorRequestsRepository.save(moderatorRequest);

        return new ApiResponse("Request submitted");
    }

    public Set<ModeratorRequestTO> getModeratorRequests() {
        return ServerToClientDataConverter.mapModeratorRequests(moderatorRequestsRepository.findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName.PENDING));
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

    // todo sprawic by zwracalo najnowszy request i napisac test
    public ModeratorRequestTO getUserRequests(Long userId) {
        ModeratorRequest moderatorRequest =
                moderatorRequestsRepository
                        .findModeratorRequestsByUser_Id(userId)
                        .orElseThrow(() -> new AppException("No entry in moderator_requests for user " + userId));

        return mapModeratorRequest(moderatorRequest);
    }

    private boolean hasRole(RoleName roleName, User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
