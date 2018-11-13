package mathandel.backend.service;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.UserTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    private final Long editionId = 1L;
    private final Long userId = 2L;
    private final String name = "name";
    private final String surname = "surname";
    private final String email = "email@emial.com";
    private final String username = "username";
    private final String newPassword = "newPassword";
    private final String address = "address";
    private final String city = "city";
    private final String country = "country";
    private final String postalCode = "postalCode";

    private Edition edition = new Edition()
            .setId(editionId)
            .setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.OPENED))
            .setMaxParticipants(10);

    private User user = new User()
            .setId(userId)
            .setName(name)
            .setSurname(surname)
            .setUsername(username)
            .setEmail(email)
            .setRoles(Collections.singleton(new Role().setName(RoleName.ROLE_USER)))
            .setCity(city)
            .setCountry(country)
            .setPostalCode(postalCode);

    private UserTO userTO = new UserTO()
            .setEmail(email)
            .setName(name)
            .setSurname(surname)
            .setUsername(username)
            .setAddress(address)
            .setCity(city)
            .setCountry(country)
            .setPostalCode(postalCode);

    @MockBean
    UserRepository userRepository;

    @MockBean
    EditionRepository editionRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Test
    public void shouldJoinEdition() {
        //given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        ApiResponse apiResponse = userService.joinEdition(userId, editionId);

        //then
        assertThat(apiResponse.getMessage()).isEqualTo("User added to edition successfully");
    }

    @Test
    public void shouldFailJoinEditionUserDoesntExist() {
        //given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.joinEdition(userId, editionId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldFailOnJoinEditionEditionDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Edition", "id", editionId);
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> userService.joinEdition(userId, editionId));
        assertThat(resourceNotFoundException).isEqualTo(expected);
    }

    // todo shouldGetUserData

    @Test
    public void shouldFailGetUserData() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("User", "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserData(userId));
        assertThat(resourceNotFoundException).isEqualTo(expected);
    }

    @Test
    public void shouldEditMyData() {
        //given
        when(userRepository.existsByUsername(userTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userTO.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        //when
        UserTO userTo = userService.editMyData(userId, userTO);

        //then
        assertThat(userTo.getUsername()).isEqualTo(userTO.getUsername());
        assertThat(userTo.getName()).isEqualTo(userTO.getName());
        assertThat(userTo.getSurname()).isEqualTo(userTO.getSurname());
        assertThat(userTo.getEmail()).isEqualTo(userTO.getEmail());
        assertThat(userTo.getAddress()).isEqualTo(userTO.getAddress());
        assertThat(userTo.getCity()).isEqualTo(userTO.getCity());
        assertThat(userTo.getCountry()).isEqualTo(userTO.getCountry());
        assertThat(userTo.getPostalCode()).isEqualTo(userTO.getPostalCode());
    }

    @Test
    public void shouldFailEditMyDataUsernameIsTaken() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user.setUsername("anotherUsername")));
        when(userRepository.existsByUsername(userTO.getUsername())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () ->  userService.editMyData(userId, userTO));
        assertThat(badRequestException.getMessage()).isEqualTo("Username is already taken");
    }

    @Test
    public void shouldFailEditMyDataEmailIsTaken() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user.setEmail("email@email.email")));
        when(userRepository.existsByUsername(userTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userTO.getEmail())).thenReturn(true);

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () ->  userService.editMyData(userId, userTO));
        assertThat(badRequestException.getMessage()).isEqualTo("Email Address already in use");
    }

    @Test
    public void shouldFailEditMyDataWhenUserDoesntExist() {
        //given
        when(userRepository.existsByUsername(userTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userTO.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMyData(userId, userTO));
        assertThat(appException.getMessage()).isEqualTo("User does not exist");
    }

    @Test
    public void shouldChangePassword() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordEncoded");

        //when
        ApiResponse apiResponse = userService.changePassword(userId, newPassword);

        //then
        assertThat(apiResponse.getMessage()).isEqualTo("Password changed successfully");
    }

    @Test
    public void shouldThrowExceptionWhenChangingPassword() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordEncoded");

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMyData(userId, userTO));
        assertThat(appException.getMessage()).isEqualTo("User does not exist");
    }
}

