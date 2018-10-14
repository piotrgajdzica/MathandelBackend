package mathandel.backend.controller;

import mathandel.backend.client.request.SignInRequest;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.client.response.JwtAuthenticationResponse;
import mathandel.backend.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static mathandel.backend.utils.UrlPaths.signInPath;
import static mathandel.backend.utils.UrlPaths.signUpPath;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(signInPath)
    public @ResponseBody JwtAuthenticationResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @PostMapping(signUpPath)
    public @ResponseBody ApiResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}
