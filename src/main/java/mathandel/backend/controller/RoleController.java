package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.client.ModeratorRequestsTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static mathandel.backend.utils.UrlPaths.moderatorRequestsGetMyRequests;
import static mathandel.backend.utils.UrlPaths.moderatorRequestsResolvePath;

@Controller
@RequestMapping("/api/moderatorRequests")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse requestModerator(@CurrentUser UserPrincipal currentUser,
                                 @Valid @RequestBody ModeratorRequestTO reason) {
        return roleService.requestModerator(reason, currentUser.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody
    Set<ModeratorRequestTO> getModeratorRequests() {
        return roleService.getModeratorRequests();
    }

    @PutMapping(moderatorRequestsResolvePath)
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody
    ApiResponse resolveModeratorRequest(@RequestBody ModeratorRequestsTO moderatorRequestsTO) {
        return roleService.resolveModeratorRequests(moderatorRequestsTO.getModeratorRequests());
    }

    @GetMapping(moderatorRequestsGetMyRequests)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ModeratorRequestTO getMyRequest(@CurrentUser UserPrincipal currentUser) {
        return roleService.getUserRequests(currentUser.getId());
    }


}
