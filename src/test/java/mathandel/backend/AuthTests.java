package mathandel.backend;

import mathandel.backend.controller.AuthController;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.SignUpRequest;
import mathandel.backend.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTests {

    @Autowired
    AuthController authController;

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldCreateUser(){

        //given
        SignUpRequest signUpRequest = new SignUpRequest();
        String name = "Kubster";
        String surname = "Jakubster";
        String email = "kubsterJakubster@gmail.com";
        String password = "rerekumkum";
        String username = "kubster96";

        //when
        signUpRequest.setName(name);
        signUpRequest.setSurname(surname);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setUsername(username);
        authController.registerUser(signUpRequest);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        //then
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Assert.assertEquals(user.getName(), name);
            Assert.assertEquals(user.getSurname(), surname);
            Assert.assertEquals(user.getEmail(), email);
            Assert.assertEquals(user.getUsername(), username);
        }
    }
}
