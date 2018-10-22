package mathandel.backend.service;

import mathandel.backend.component.DBDataInitializer;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResultServiceTest {

    private SignUpRequest signUpRequest = new SignUpRequest()
            .setUsername("username")
            .setEmail("email@email.com")
            .setName("name")
            .setSurname("surname")
            .setPassword("password");

    @MockBean
    UserRepository userRepository;

    @MockBean
    RateRepository rateRepository;

    @MockBean
    ResultRepository resultRepository;

    @Autowired
    ResultService resultService;

    @MockBean
    DBDataInitializer DBDataInitializer;

    @Test
    public void shouldSignUp() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role().setName(RoleName.ROLE_USER)));

        //when
        ApiResponse apiResponse = authService.signUp(signUpRequest);

        //then
        assertThat(apiResponse.getMessage()).isEqualTo("User registered successfully");
    }


}