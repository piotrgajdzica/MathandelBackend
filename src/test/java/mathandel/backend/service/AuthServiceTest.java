package mathandel.backend.service;

import mathandel.backend.component.DataLoaderComponent;
import mathandel.backend.exception.AppException;
import mathandel.backend.model.Role;
import mathandel.backend.model.RoleName;
import mathandel.backend.payload.request.SignUpRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    private SignUpRequest signUpRequest = new SignUpRequest()
            .setUsername("username")
            .setEmail("email@email.com")
            .setName("name")
            .setSurname("surname")
            .setPassword("password");

    @MockBean
    UserRepository userRepository;

    @MockBean
    EditionStatusRepository editionStatusRepository;

    @MockBean
    EditionRepository editionRepository;

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    AuthService authService;

    @MockBean
    DataLoaderComponent dataLoaderComponent;

    @Test
    public void shouldSignUp() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role().setName(RoleName.ROLE_USER)));

        //when
        ApiResponse apiResponse = authService.signUp(signUpRequest);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User registered successfully");
    }

    @Test
    public void shouldThrowAppException() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());
        //when then
        AppException appException = assertThrows(AppException.class, () -> authService.signUp(signUpRequest));
        assertThat(appException.getMessage()).isEqualTo(("User Role not set."));
    }

    @Test
    public void shouldAlreadyExistsByEmail() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        //when
        ApiResponse apiResponse = authService.signUp(signUpRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Email Address already in use.");

    }

    @Test
    public void shouldAlreadyExistsByUsername() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        //when
        ApiResponse apiResponse = authService.signUp(signUpRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Username is already taken.");

    }
}