package mathandel.backend.controller;

import mathandel.backend.payload.request.SignUpRequest;
import mathandel.backend.payload.response.GenericResponse;
import mathandel.backend.service.ServiceResponse;
import mathandel.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        ServiceResponse serviceResponse = userService.createUser(signUpRequest);
        if(serviceResponse.getSuccess()){
            return ResponseEntity.ok().body(serviceResponse);
        }
        return ResponseEntity.badRequest().body(serviceResponse);

    }
}
