package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.RateTO;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.server.Result;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.RateTypeName;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.RateRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

//todo why is it ignored??
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class RateTypeServiceTest {

    private RateTO rateTO = new RateTO();
    private final Long resultId = 1L;
    private final Long receiverId = 1L;
    private final Long notReceiverId = 2L;
    private RateTypeName rateTypeName = RateTypeName.POSITIVE;

    @Mock
    Result result;

    @Mock
    User receiver;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RateRepository rateRepository;

    @MockBean
    ResultRepository resultRepository;

    @Autowired
    RateService rateService;

    @After
    public void tearDown() {
        clearInvocations(result, receiver, userRepository, rateRepository, resultRepository);
    }

    //todo
//    @Test
//    public void shouldRateResult() {
//        //given
//        initializeTransactionRateTO();
//
//        when(resultRepository.findById(resultId)).thenReturn(Optional.of(result));
//        when(result.getReceiver()).thenReturn(receiver);
//        when(receiver.getId()).thenReturn(receiverId);
//        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
//
//        //when
//        ApiResponse apiResponse = rateService.rateResult(receiverId, resultId, rateTO);
//
//        //then
//        assertEquals(apiResponse.getMessage(), "Result rated successfully");
//    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotReceiverOfItemThatHeWantsToRate() {
        //given
        initializeTransactionRateTO();

        when(resultRepository.findById(resultId)).thenReturn(Optional.of(result));
        when(result.getReceiver()).thenReturn(receiver);
        when(receiver.getId()).thenReturn(receiverId);
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));


        //when & then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> rateService.rateResult(notReceiverId, resultId, rateTO));

        assertEquals(badRequestException.getMessage(), "User is not receiver of role");
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotInDB() {
        //given
        initializeTransactionRateTO();

        when(resultRepository.findById(resultId)).thenReturn(Optional.of(result));
        when(result.getReceiver()).thenReturn(receiver);
        when(receiver.getId()).thenReturn(receiverId);
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        //when & then
        AppException appException = assertThrows(AppException.class, () -> rateService.rateResult(receiverId, resultId, rateTO));

        assertEquals(appException.getMessage(), "User not in db");
    }

    @Test
    public void shouldThrowExceptionWhenResultDoesntExist() {
        //given
        initializeTransactionRateTO();

        when(resultRepository.findById(resultId)).thenReturn(Optional.empty());

        //when & then
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> rateService.rateResult(receiverId, resultId, rateTO));

        assertEquals(badRequestException.getMessage(), "Result doesn't exist");
    }

    private void initializeTransactionRateTO() {
        rateTO
                .setResultId(resultId)
                .setRateTypeName(rateTypeName)
                .setComment("comment");
    }
}