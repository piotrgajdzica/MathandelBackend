package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.Role;
import mathandel.backend.model.User;
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

    private Edition edition = new Edition()
            .setId(editionId);

    private User user = new User()
            .setId(userId)
            .setName("name")
            .setSurname("surname")
            .setUsername("username")
            .setEmail("email")
            .setRoles(Collections.singleton(new Role()));

    @MockBean
    UserRepository userRepository;

    @MockBean
    EditionRepository editionRepository;

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




}

