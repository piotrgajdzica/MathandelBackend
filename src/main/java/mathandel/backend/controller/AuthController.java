package mathandel.backend.controller;

import mathandel.backend.client.request.SignInRequest;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.client.response.JwtAuthenticationResponse;
import mathandel.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signin")
    public @ResponseBody JwtAuthenticationResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signup")
    public @ResponseBody ApiResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }
}
