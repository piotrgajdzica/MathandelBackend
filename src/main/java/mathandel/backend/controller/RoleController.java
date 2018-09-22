package mathandel.backend.controller;

import mathandel.backend.model.ModeratorRequest;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.ModeratorRequestReasonRequest;
import mathandel.backend.payload.request.ModeratorRequestStatusChangeRequest;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/api/moderator-requests")
public class RoleController {

    ModeratorRequestsRepository moderatorRequestsRepository;

    UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestModerator(@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody ModeratorRequestReasonRequest reason) {
        Optional<User> userOptional =userRepository.findById(currentUser.getId());

        if(!userOptional.isPresent()) {

            ModeratorRequest moderatorRequest = new ModeratorRequest();
//            moderatorRequest.s

//            moderatorRequestsRepository.save();
        }
        // sprawdzic czy juz nie ma requestu
        // dodac
        // todo
        return null;

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
