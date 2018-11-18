package mathandel.backend.service;

import mathandel.backend.model.client.request.SignUpRequest;
import mathandel.backend.component.SystemInitializer;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
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

//todo teste signUp and to signIn
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {

    private SignUpRequest signUpRequest = new SignUpRequest()
            .setUsername("username")
            .setEmail("email@email.com")
            .setName("name")
            .setSurname("surname")
            .setPassword("password")
            .setAddress("address");

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
    SystemInitializer SystemInitializer;

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
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role().setName(RoleName.ROLE_USER)));
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authService.signUp(signUpRequest));
        assertThat(badRequestException.getMessage()).isEqualTo("Email Address already in use");
    }

    @Test
    public void shouldFailOnSignUpUsernameInUse() {
        //given
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role().setName(RoleName.ROLE_USER)));
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authService.signUp(signUpRequest));
        assertThat(badRequestException.getMessage()).isEqualTo("Username is already in use");
    }
}