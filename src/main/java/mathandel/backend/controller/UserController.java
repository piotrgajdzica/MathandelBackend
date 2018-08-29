package mathandel.backend.controller;

import mathandel.backend.payload.request.EditMeRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUser(@PathVariable("userID") String userID) {
        return ResponseEntity.ok(userService.getUser(userID));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.getMe(currentUser.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> editMe(@CurrentUser UserPrincipal userPrincipal, @RequestBody EditMeRequest editMeRequest){
        ApiResponse apiResponse = userService.editMe(userPrincipal.getId(), editMeRequest);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }
}
