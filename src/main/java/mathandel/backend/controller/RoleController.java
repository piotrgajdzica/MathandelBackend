package mathandel.backend.controller;

import mathandel.backend.payload.request.ModeratorRequestReasonRequest;
import mathandel.backend.payload.response.ApiResponse;
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

    @Autowired
    RoleService roleService;

    // todo złe urle
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestModerator(@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody ModeratorRequestReasonRequest reason) {
        ApiResponse apiResponse = roleService.requestModerator(reason, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getModeratorRequests() {
        return ResponseEntity.ok(roleService.getModeratorRequests());
    }

    @PutMapping("resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolveModeratorRequest(@RequestBody List<ModeratorRequestTO> moderatorRequestMessageRequests) {
        ApiResponse apiResponse = roleService.resolveModeratorRequests(moderatorRequestMessageRequests);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping("my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRequest(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(roleService.getUserRequests(currentUser.getId()));
    }


}
