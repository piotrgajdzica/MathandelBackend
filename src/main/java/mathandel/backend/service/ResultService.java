package mathandel.backend.service;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.ResultTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.TransactionRateTO;
import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.TransactionRate;
import mathandel.backend.model.server.Result;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.TransactionRateRepository;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service

public class ResultService {


    private ResultRepository resultRepository;
    private UserRepository userRepository;
    private TransactionRateRepository transactionRateRepository;
    private ProductRepository productRepository;

    public ResultService(UserRepository userRepositor, ResultRepository resultRepository, TransactionRateRepository transactionRateRepository, ProductRepository productRepository) {
        this.userRepository = userRepositor;
        this.resultRepository = resultRepository;
        this.transactionRateRepository = transactionRateRepository;
        this.productRepository = productRepository;
    }

    // todo close edition


    public ApiResponse rateResult(Long userId, TransactionRateTO transactionRateTO) {

        Result result = resultRepository
                .findById(transactionRateTO.getResultId()).orElseThrow(() -> new BadRequestException("Result doesn't exist"));

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

    public Set<ResultTO> getEditionProductsToSendForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllBySender_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<ResultTO> getEditionProductsToReceiveForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllByReceiver_IdAndEdition_Id(userId, editionId)) ;
    }
}
