package mathandel.backend.controller;

import mathandel.backend.payload.request.ModeratorRequestReasonRequest;
import mathandel.backend.payload.request.ModeratorRequestStatusChangeRequest;
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

@Controller
@RequestMapping("/api/moderator-requests")
public class RoleController {

    @Autowired
    RoleService roleService;


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
        //  wyciagnac requesty z bazy, zwrocic
        // todo
        return null;
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolveModeratorRequest(@RequestBody ModeratorRequestStatusChangeRequest moderatorRequestMessageRequest) {
        //  zupdatowac
        // todo

        return null;
    }

    @GetMapping("my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRequest(@CurrentUser UserPrincipal currentUser) {
        //  wyciagnac requesty z bazy, zwrocic
        // dodac user response
        // todo
        return null;

    }


}
