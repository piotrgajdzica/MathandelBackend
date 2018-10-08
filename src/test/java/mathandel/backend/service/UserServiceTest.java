package mathandel.backend.service;

import mathandel.backend.client.model.UserTO;
import mathandel.backend.client.request.UserDataRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.Role;
import mathandel.backend.model.User;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static mathandel.backend.service.UserService.mapMyData;
import static mathandel.backend.service.UserService.mapUserData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private Long editionId = 1L;
    private Long userId = 2L;
    private String name = "name";
    private String surname = "surname";
    private String email = "email@emial.com";
    private String username = "username";
    private String newPassword = "newPassword";

    private Edition edition = new Edition()
            .setId(editionId)
            .setMaxParticipants(10);

    private User user = new User()
            .setId(userId)
            .setName(name)
            .setSurname(surname)
            .setUsername(username)
            .setEmail(email)
            .setRoles(Collections.singleton(new Role()));

    private UserDataRequest userDataRequest = new UserDataRequest()
            .setEmail(email)
            .setName(name)
            .setSurname(surname)
            .setUsername(username);

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
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User added to edition successfully");
    }

    @Test
    public void shouldThrowExceptionOnJoinEditionUserDoesntExist() {
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
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        ApiResponse apiResponse = userService.joinEdition(userId, editionId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition doesn't exist");
    }

    @Test
    public void shouldGetUser() {
        //given
        UserTO expected = mapUserData(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        UserTO actual = userService.getUserData(userId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionOnGetUser() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> userService.getUserData(userId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("User");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(userId);
    }

    @Test
    public void shouldGetMe() {
        //given
        UserTO expected = mapMyData(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        UserTO actual = userService.getMyData(userId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionOnGetMe() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> userService.getMyData(userId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("User");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(userId);
    }

    @Test
    public void shouldEditMe() {
        //given
        when(userRepository.existsByUsername(userDataRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDataRequest.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        ApiResponse apiResponse = userService.editMyData(userId, userDataRequest);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Successfully edited user");
    }

    @Test
    public void shoudlFailEditMeWhenUsernameIsTaken() {
        //given
        when(userRepository.existsByUsername(userDataRequest.getUsername())).thenReturn(true);

        //when
        ApiResponse apiResponse = userService.editMyData(userId, userDataRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Username is already taken");
    }

    @Test
    public void shouldFailEditMeWhenEmailIsTaken() {
        //given
        when(userRepository.existsByUsername(userDataRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDataRequest.getEmail())).thenReturn(true);

        //when
        ApiResponse apiResponse = userService.editMyData(userId, userDataRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Email Address already in use");
    }

    @Test
    public void shouldFailEditMeWhenUserDoesntExist() {
        //given
        when(userRepository.existsByUsername(userDataRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDataRequest.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMyData(userId, userDataRequest));
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
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Password changed successfully");
    }

    @Test
    public void shouldThrowExceptionWhenChanginhPassword() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordEncoded");

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMyData(userId, userDataRequest));
        assertThat(appException.getMessage()).isEqualTo("User does not exist");
    }
}

