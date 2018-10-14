package mathandel.backend.controller;

import mathandel.backend.client.model.UserTO;
import mathandel.backend.client.request.PasswordRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(userPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody UserTO getUserData(@PathVariable Long userId) {
        return userService.getUserData(userId);
    }

    @GetMapping(userMePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody UserTO getMyData(@CurrentUser UserPrincipal currentUser) {
        return userService.getUserData(currentUser.getId());
    }

    @PutMapping(userMePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse editMyData(@CurrentUser UserPrincipal userPrincipal,
                                                @RequestBody UserTO userTO){
        return userService.editMyData(userPrincipal.getId(), userTO);
    }

    @PutMapping(userMePasswordPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse changePassword(@CurrentUser UserPrincipal userPrincipal,
                                                    @RequestBody PasswordRequest passwordRequest){
        return userService.changePassword(userPrincipal.getId(), passwordRequest.getNewPassword());
    }
}
