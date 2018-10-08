package mathandel.backend.service;

import mathandel.backend.client.model.EditionTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.model.User;
import mathandel.backend.client.request.EditionDataRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EditionServiceTest {

    private Long userId = 1L;
    private User user = new User("James", "Smith", "jsmith", "jsmith@gmail.com", "jsmith123");

    private EditionDataRequest editionDataRequest = new EditionDataRequest()
            .setName("Mathandel 4000")
            .setMaxParticipants(100)
            .setEndDate(LocalDate.now().plusMonths(2));

    private Long editionId = 1L;
    private Edition edition = new Edition();

    @MockBean
    UserRepository userRepository;

    @MockBean
    EditionStatusTypeRepository editionStatusTypeRepository;

    @MockBean
    EditionRepository editionRepository;

    @Autowired
    EditionService editionService;

    @Test
    public void shouldCreateEdition() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.OPENED)).thenReturn(new EditionStatusType(EditionStatusName.OPENED));

        // when
        ApiResponse apiResponse = editionService.createEdition(editionDataRequest, userId);

        // then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition added successfully");
    }

    @Test
    public void shouldThrowExceptionWhenCreateUserDoesntExist() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        AppException appException = assertThrows(AppException.class, () -> editionService.createEdition(editionDataRequest, userId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");

    }

    @Test
    public void shouldFailCreateEditionAlreadyExists() {
        // given
        when(editionRepository.existsByName(editionDataRequest.getName())).thenReturn(true);

        // when
        ApiResponse apiResponse = editionService.createEdition(editionDataRequest, userId);

        // then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition name already exists.");
    }

    @Test
    public void shouldThrowExceptionWhenEditUserDoesntExist() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when then
        AppException appException = assertThrows(AppException.class, () -> editionService.editEdition(editionDataRequest, editionId, userId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldFailModeratorErrorResponse() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        ApiResponse apiResponse = editionService.editEdition(editionDataRequest, editionId, userId);

        // then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("You are not moderator of this edition");
    }

    @Test
    public void shouldReturnEditionNameAlreadyTakenResponse() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionDataRequest.getName())).thenReturn(true);

        // when
        ApiResponse apiResponse = editionService.editEdition(editionDataRequest, editionId, userId);

        // then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition name already exists");
    }

    @Test
    public void shouldReturnEditionDateErrorResponse() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionDataRequest.getName())).thenReturn(false);
        editionDataRequest.setEndDate(LocalDate.now().minusDays(1));

        // when
        ApiResponse apiResponse = editionService.editEdition(editionDataRequest, editionId, userId);

        // then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition end date cannot be in the past");
    }

    @Test
    public void shouldEditEdition() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionDataRequest.getName())).thenReturn(false);
        editionDataRequest.setEndDate(LocalDate.now().plusDays(1));

        // when
        ApiResponse apiResponse = editionService.editEdition(editionDataRequest, editionId, userId);

        // then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition edited successfully");
    }

    @Test
    public void shouldGetEditionList() {
        // given
        List<Edition> editions = new ArrayList<>(Arrays.asList(new Edition(), new Edition()));
        when(editionRepository.findAll()).thenReturn(editions);

        // when
        List<EditionTO> resultEdition = editionService.getEditions();

        // then
        assertThat(resultEdition.size()).isEqualTo(2);
    }
}