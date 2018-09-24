package mathandel.backend.service;

import mathandel.backend.model.*;
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

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModeratorRequestsRepository moderatorRequestsRepository;


    @Autowired
    UserRepository userRepository;

    public ApiResponse requestModerator(@Valid ModeratorRequestReasonRequest reason, Long userId) {
        ApiResponse apiResponse;

        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isPresent() && hasRole(RoleName.ROLE_MODERATOR, optUser.get()))
            return new ApiResponse(false, "Role already given");

        Optional<ModeratorRequest> optModeeratorRequest = moderatorRequestsRepository.findByUser(optUser.get());

        if (optModeeratorRequest.isPresent())
            return new ApiResponse(false, "Request already submitted");

        ModeratorRequest moderatorRequest = new ModeratorRequest();

        ModeratorRequestStatus moderatorRequestStatus = new ModeratorRequestStatus();
        moderatorRequestStatus.setName(ModeratorRequestStatusName.PENDING);

        moderatorRequest.setUser(optUser.get());
        moderatorRequest.setReason(reason.getReason());
        moderatorRequest.setModeratorRequestStatus(moderatorRequestStatus);
        moderatorRequestsRepository.save(moderatorRequest);

        return new ApiResponse(true, "Request submitted");
    }

    public List<ModeratorRequest> getModeratorRequests() {
        return  moderatorRequestsRepository.findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName.PENDING);
    }



    private boolean hasRole(RoleName roleName, User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
