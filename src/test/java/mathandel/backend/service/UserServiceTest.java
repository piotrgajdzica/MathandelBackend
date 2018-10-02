package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.Role;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.EditMeRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.payload.response.GetMeResponse;
import mathandel.backend.payload.response.GetUserResponse;
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
            .setId(editionId);

    private User user = new User()
            .setId(userId)
            .setName(name)
            .setSurname(surname)
            .setUsername(username)
            .setEmail(email)
            .setRoles(Collections.singleton(new Role()));

    private EditMeRequest editMeRequest = new EditMeRequest()
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
    public void shouldThrowExceptionOnJoinEditionEditionDoesntExist() {
        //given
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.joinEdition(userId, editionId));
        assertThat(appException.getMessage()).isEqualTo("Edition doesn't exist");
    }

    @Test
    public void shouldGetUser() {
        //given
        GetUserResponse expected = new GetUserResponse()
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setRoles(user.getRoles());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        GetUserResponse actual = userService.getUser(userId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionOnGetUser() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> userService.getUser(userId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("User");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(userId);
    }

    @Test
    public void shouldGetMe() {
        //given
        GetMeResponse expected = new GetMeResponse()
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setRoles(user.getRoles())
                .setEditions(user.getEditions());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        GetMeResponse actual = userService.getMe(userId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionOnGetMe() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> userService.getMe(userId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("User");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(userId);
    }

    @Test
    public void shouldEditMe() {
        //given
        when(userRepository.existsByUsername(editMeRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(editMeRequest.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        ApiResponse apiResponse = userService.editMe(userId, editMeRequest);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Successfully edited user.");
    }

    @Test
    public void shoudlFailEditMeWhenUsernameIsTaken() {
        //given
        when(userRepository.existsByUsername(editMeRequest.getUsername())).thenReturn(true);

        //when
        ApiResponse apiResponse = userService.editMe(userId, editMeRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Username is already taken.");
    }

    @Test
    public void shouldFailEditMeWhenEmailIsTaken() {
        //given
        when(userRepository.existsByUsername(editMeRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(editMeRequest.getEmail())).thenReturn(true);

        //when
        ApiResponse apiResponse = userService.editMe(userId, editMeRequest);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Email Address already in use.");
    }

    @Test
    public void shouldFailEditMeWhenUserDoesntExist() {
        //given
        when(userRepository.existsByUsername(editMeRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(editMeRequest.getEmail())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMe(userId, editMeRequest));
        assertThat(appException.getMessage()).isEqualTo("User does not exist.");
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
        assertThat(apiResponse.getMessage()).isEqualTo("Password changed successfully.");
    }

    @Test
    public void shouldThrowExceptionWhenChanginhPassword() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordEncoded");

        //when then
        AppException appException = assertThrows(AppException.class, () -> userService.editMe(userId, editMeRequest));
        assertThat(appException.getMessage()).isEqualTo("User does not exist.");
    }
}

