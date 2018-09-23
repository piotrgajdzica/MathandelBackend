package mathandel.backend.service;

import mathandel.backend.model.EditionStatus;
import mathandel.backend.model.EditionStatusName;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.AddEditEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EditionServiceTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    EditionStatusRepository editionStatusRepository;

    @MockBean
    EditionRepository editionRepository;

    @Autowired
    EditionService editionService;

    @Test
    public void shouldCreateEdition() {
        //given
        String userName = "jsmith";
        User user = new User("James", "Smith", "jsmith", "jsmith@gmail.com", "jsmith123");

        AddEditEditionRequest addEditEditionRequest = new AddEditEditionRequest()
                .setName("Mathandel 4000")
                .setMaxUsers(100)
                .setEndDate(LocalDate.of(2014, Month.JANUARY, 1));

        when(userRepository.findByUsername(userName)).thenReturn(user);
        when(editionStatusRepository.findByEditionStatusName(EditionStatusName.OPENED)).thenReturn(new EditionStatus(EditionStatusName.OPENED));

        //when
        ApiResponse apiResponse = editionService.createEdition(addEditEditionRequest, userName);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition added successfully");
    }
}