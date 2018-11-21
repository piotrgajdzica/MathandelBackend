package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import mathandel.backend.repository.ModeratorRequestsRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

//todo why is it ignored??
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModeratorRequestServiceTest {

    private Long requestId = 1L;
    private Long userId = 1L;
    private ModeratorRequestTO moderatorRequestTO;
    private ModeratorRequestStatusName moderatorRequestStatusName = ModeratorRequestStatusName.PENDING;
    private ModeratorRequestStatus moderatorRequestStatus = new ModeratorRequestStatus();

    @MockBean
    ModeratorRequestsRepository moderatorRequestsRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ModeratorRequestService moderatorRequestService;

    @Mock
    User user;

    @Mock
    ModeratorRequest moderatorRequest;

    @After
    public void tearDown() {
        clearInvocations(moderatorRequestsRepository, user, userRepository, moderatorRequest);
    }

    //todo fix
//    @Test
//    public void shouldRequestModerator() {
//        //given
//        moderatorRequestTO = new ModeratorRequestTO()
//                .setModeratorRequestStatus(moderatorRequestStatusName)
//                .setReason("reason");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(moderatorRequestsRepository.existsByUser(user)).thenReturn(false);
//
//        // when
//        ApiResponse apiResponse = moderatorRequestService.requestModerator(moderatorRequestTO, userId);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Request submitted");
//    }

    @Test
    public void shouldThrowExceptionWhenRequestAlreadyExists() {
        //given
        moderatorRequestTO = new ModeratorRequestTO()
                .setModeratorRequestStatus(moderatorRequestStatusName)
                .setReason("reason");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(moderatorRequestsRepository.existsByUser(user)).thenReturn(true);

        // when & then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> moderatorRequestService.requestModerator(moderatorRequestTO, userId));
        assertEquals(badRequestException.getMessage(), "Request already submitted");
    }

    @Test
    public void shouldThrowExceptionWhenUserNotInDatabase() {
        //given
        moderatorRequestTO = new ModeratorRequestTO()
                .setModeratorRequestStatus(moderatorRequestStatusName)
                .setReason("reason");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(moderatorRequestsRepository.existsByUser(user)).thenReturn(true);

        // when && then
        AppException appException = assertThrows(AppException.class, () -> moderatorRequestService.requestModerator(moderatorRequestTO, userId));
        assertEquals(appException.getMessage(), "User doesn't exist");
    }

    //todo
//    @Test
//    public void shouldResolveModeratorRequests() {
//        // given
//        moderatorRequestTO = new ModeratorRequestTO()
//                .setId(requestId)
//                .setModeratorRequestStatus(moderatorRequestStatusName)
//                .setReason("reason")
//                .setUserId(userId);
//
//        Set<ModeratorRequestTO> moderatorRequestTOs = new HashSet<>();
//        moderatorRequestTOs.add(moderatorRequestTO);
//
//        when(moderatorRequestsRepository.findById(requestId)).thenReturn(Optional.of(moderatorRequest));
//        when(moderatorRequest.getModeratorRequestStatus()).thenReturn(moderatorRequestStatus);
//
//        // when
//        ApiResponse apiResponse = moderatorRequestService.resolveModeratorRequests(moderatorRequestTOs);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Requests resolved");
//    }

    //todo
//    @Test
//    public void shouldThrowAnExceptionWhenRequestNotInDb() {
//        // given
//        moderatorRequestTO = new ModeratorRequestTO()
//                .setId(requestId)
//                .setModeratorRequestStatus(moderatorRequestStatusName)
//                .setReason("reason")
//                .setUserId(userId);
//
//        Set<ModeratorRequestTO> moderatorRequestTOs = new HashSet<>();
//        moderatorRequestTOs.add(moderatorRequestTO);
//        when(moderatorRequestsRepository.findById(requestId)).thenReturn(Optional.empty());
//
//        // when & then
//        AppException appException = assertThrows(AppException.class, () -> moderatorRequestService.resolveModeratorRequests(moderatorRequestTOs));
//        assertEquals(appException.getMessage(), "No entry in moderator_requests for user " + moderatorRequestTO.getUserId());
//    }
}
