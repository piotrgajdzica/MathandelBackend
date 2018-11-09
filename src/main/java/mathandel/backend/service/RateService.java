package mathandel.backend.service;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.TransactionRateTO;
import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.Result;
import mathandel.backend.model.server.TransactionRate;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.TransactionRateRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RateService {

    private ResultRepository resultRepository;
    private UserRepository userRepository;
    private TransactionRateRepository transactionRateRepository;

    public RateService(ResultRepository resultRepository, UserRepository userRepository, TransactionRateRepository transactionRateRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.transactionRateRepository = transactionRateRepository;
    }

    public ApiResponse rateResult(Long userId, TransactionRateTO transactionRateTO) {

        Result result = resultRepository
                .findById(transactionRateTO.getResultId()).orElseThrow(() -> new ResourceNotFoundException("Result","id" ,transactionRateTO.getResultId()));

        if (!userId.equals(result.getReceiver().getId())) {
            throw new BadRequestException("User is not receiver of role");
        }

        TransactionRate transactionRate = new TransactionRate()
                .setRater(userRepository.findById(userId).orElseThrow(() -> new AppException("User not in db")))
                .setRate(new Rate().setName(transactionRateTO.getRateName()))
                .setComment(transactionRateTO.getComment())
                .setResult(result);

        transactionRateRepository.save(transactionRate);

        return new ApiResponse("Result rated succesfully");
    }

    public Set<TransactionRateTO> getUserRates(Long id) {
        return ServerToClientDataConverter.mapRates(transactionRateRepository.findAllByResult_Sender_Id(id)) ;
    }
}
