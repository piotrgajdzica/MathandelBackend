package mathandel.backend.controller;

import mathandel.backend.model.client.UserDataTO;
import mathandel.backend.model.client.UserTO;
import mathandel.backend.model.client.request.PasswordRequest;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // documented
    @GetMapping(userPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    UserDataTO getUserData(@PathVariable Long userId) {
        return userService.getUserData(userId);
    }

    // documented
    @GetMapping(userMePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    UserTO getMyData(@CurrentUser UserPrincipal currentUser) {
        return userService.getUser(currentUser.getId());
    }

    // documented
    @PutMapping(userMePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    UserTO editMyData(@CurrentUser UserPrincipal userPrincipal,
                      @RequestBody @Valid UserTO userTO) {
        return userService.editMyData(userPrincipal.getId(), userTO);
    }

    // documented
    @PutMapping(userMePasswordPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse changePassword(@CurrentUser UserPrincipal userPrincipal,
                               @RequestBody @Valid PasswordRequest passwordRequest) {
        return userService.changePassword(userPrincipal.getId(), passwordRequest.getNewPassword());
    }
}
