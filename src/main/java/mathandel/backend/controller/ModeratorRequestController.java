package mathandel.backend.controller;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.client.ModeratorRequestsTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ModeratorRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ModeratorRequestController {

    private final ModeratorRequestService moderatorRequestService;

    public ModeratorRequestController(ModeratorRequestService moderatorRequestService) {
        this.moderatorRequestService = moderatorRequestService;
    }

    @PostMapping(moderatorRequestsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse requestModerator(@CurrentUser UserPrincipal currentUser,
                                 @Valid @RequestBody ModeratorRequestTO reason) {
        return moderatorRequestService.requestModerator(reason, currentUser.getId());
    }

    @GetMapping(moderatorRequestsPath)
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody
    Set<ModeratorRequestTO> getModeratorRequests() {
        return moderatorRequestService.getPendingModeratorRequests();
    }

    @PutMapping(moderatorRequestsResolvePath)
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody
    ApiResponse resolveModeratorRequest(@RequestBody ModeratorRequestsTO moderatorRequestsTO) {
        return moderatorRequestService.resolveModeratorRequests(moderatorRequestsTO.getModeratorRequests());
    }

    @GetMapping(moderatorRequestsGetMyRequests)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ModeratorRequestTO> getMyRequest(@CurrentUser UserPrincipal currentUser) {
        return moderatorRequestService.getUserRequests(currentUser.getId());
    }
}
