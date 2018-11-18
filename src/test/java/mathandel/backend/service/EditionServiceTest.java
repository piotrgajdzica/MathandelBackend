package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EditionServiceTest {

    private final Long userId = 1L;
    private User user = new User()
            .setId(userId)
            .setName("James")
            .setSurname("Smith")
            .setUsername("jsmith")
            .setEmail("jsmith@gmail.com")
            .setPassword("jsmith123");

    private User admin = new User()
            .setName("admin")
            .setSurname("admin")
            .setUsername("admin")
            .setEmail("admin@admin.admin")
            .setPassword("admin");

    private EditionTO editionTO = new EditionTO()
            .setName("Mathandel 4000")
            .setMaxParticipants(100)
            .setEndDate(LocalDate.now().plusMonths(2))
            .setDescription("Hello Darkness my old friend");

    private Long editionId = 1L;
    private Edition edition = new Edition()
            .setName("Mathandel 4000")
            .setEndDate(LocalDate.now().plusMonths(2))
            .setDescription("Hello Darkness my old friend")
            .setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.OPENED));

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
        when(userRepository.findByRolesContains(any())).thenReturn(Optional.of(admin));
        when(editionRepository.save(any())).thenReturn(edition);

        // when
        EditionTO edition = editionService.createEdition(editionTO, userId);

        // then
        assertThat(edition.getEndDate()).isEqualTo(editionTO.getEndDate());
        assertThat(edition.getName()).isEqualTo(editionTO.getName());
        assertThat(edition.getDescription()).isEqualTo(editionTO.getDescription());
    }

    @Test
    public void shouldFailOnCreateUserDoesntExist() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findByRolesContains(any())).thenReturn(Optional.of(admin));

        // when
        AppException appException = assertThrows(AppException.class, () -> editionService.createEdition(editionTO, userId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldFailCreateEditionAlreadyExists() {
        // given
        when(editionRepository.existsByName(editionTO.getName())).thenReturn(true);

        // when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> editionService.createEdition(editionTO, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("Edition name already exists");
    }

    @Test
    public void shouldFailOnEditUserDoesntExist() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when then
        AppException appException = assertThrows(AppException.class, () -> editionService.editEdition(editionTO, editionId, userId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldFailOnEditEditionNotModerator() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> editionService.editEdition(editionTO, editionId, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("You are not moderator of this edition");
    }

    @Test
    public void shouldFailOnEditEditionNameAlreadyExists() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionTO.getName())).thenReturn(true);

        // when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> editionService.editEdition(editionTO, editionId, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("Edition name already exists");
    }

    @Test
    public void shouldFailEditEditionDateError() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionTO.getName())).thenReturn(false);
        editionTO.setEndDate(LocalDate.now().minusDays(1));

        // when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> editionService.editEdition(editionTO, editionId, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("Edition end date cannot be in the past");
    }

    @Test
    public void shouldEditEdition() {
        // given
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        edition.getModerators().add(user);
        when(editionRepository.existsByName(editionTO.getName())).thenReturn(false);
        editionTO.setEndDate(LocalDate.now().plusDays(1));
        when(editionRepository.save(any())).thenReturn(edition);

        // when
        EditionTO edition = editionService.editEdition(editionTO, editionId, userId);

        // then
        assertThat(edition.getEndDate()).isEqualTo(editionTO.getEndDate());
        assertThat(edition.getName()).isEqualTo(editionTO.getName());
        assertThat(edition.getDescription()).isEqualTo(editionTO.getDescription());
    }

    //todo shouldGetEditionList
}