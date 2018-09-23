package mathandel.backend.service;

import mathandel.backend.controller.AuthController;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.SignUpRequest;
import mathandel.backend.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    AuthController authController;

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldCreateUser() {

        //given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Kubster");
        signUpRequest.setSurname("Jakubster");
        signUpRequest.setEmail("kubsterJakubster@gmail.com");
        signUpRequest.setPassword("rerekumkum");
        signUpRequest.setUsername("kubster96");

        //when
        authController.signUp(signUpRequest);
        User user = userRepository.findByEmail("kubsterJakubster@gmail.com");

        //then
        if (user != null) {
            Assert.assertEquals(user.getName(), "Kubster");
            Assert.assertEquals(user.getSurname(), "Jakubster");
            Assert.assertEquals(user.getEmail(), "kubsterJakubster@gmail.com");
            Assert.assertEquals(user.getUsername(), "kubster96");
        }
    }
}