package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/moderatorRequests")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    // todo złe urle
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse requestModerator(@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody ModeratorRequestTO reason) {
        return roleService.requestModerator(reason, currentUser.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody List<ModeratorRequestTO> getModeratorRequests() {
        return roleService.getModeratorRequests();
    }

    //todo tochybatrzebaowrapowacwobiekt
    @PutMapping("resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ApiResponse resolveModeratorRequest(@RequestBody List<ModeratorRequestTO> moderatorRequestMessageRequests) {
        return roleService.resolveModeratorRequests(moderatorRequestMessageRequests);
    }

    @GetMapping("my")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ModeratorRequestTO getMyRequest(@CurrentUser UserPrincipal currentUser) {
        return roleService.getUserRequests(currentUser.getId());
    }


}
