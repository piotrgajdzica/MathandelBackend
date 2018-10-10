package mathandel.backend.controller;

import mathandel.backend.client.model.UserTO;
import mathandel.backend.client.request.PasswordRequest;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static mathandel.backend.utils.ResponseCreator.createResponse;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserData(@PathVariable("userID") Long userID) {
        return ResponseEntity.ok(userService.getUserData(userID));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyData(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.getMyData(currentUser.getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editMyData(@CurrentUser UserPrincipal userPrincipal, @RequestBody UserTO userTO){
        return createResponse(userService.editMyData(userPrincipal.getId(), userTO));
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal userPrincipal, @RequestBody PasswordRequest passwordRequest){
        return createResponse(userService.changePassword(userPrincipal.getId(), passwordRequest.getNewPassword()));
    }
}
