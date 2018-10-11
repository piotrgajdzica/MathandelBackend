package mathandel.backend.service;

import mathandel.backend.component.DBDataInitializer;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.Role;
import mathandel.backend.model.enums.RoleName;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
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
    EditionStatusTypeRepository editionStatusTypeRepository;

    @MockBean
    EditionRepository editionRepository;

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    AuthService authService;

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
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User registered successfully");
    }

    @Test
    public void shouldNotFindUserRole() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> authService.signUp(signUpRequest));
        assertThat(appException.getMessage()).isEqualTo(("User Role not in database"));
    }

    @Test
    public void shouldAlreadyExistsByEmail() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authService.signUp(signUpRequest));
        assertThat(badRequestException.getMessage()).isEqualTo("Email Address already in use");
    }

    @Test
    public void shouldFailOnSignUpUsernameInUse() {
        //given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authService.signUp(signUpRequest));
        assertThat(badRequestException.getMessage()).isEqualTo("Username is already in use");
    }
}