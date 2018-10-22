package mathandel.backend.service;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.ResultTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.client.RateTO;
import mathandel.backend.model.server.Product;
import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.Result;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.RateRepository;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service

public class ResultService {


    private ResultRepository resultRepository;
    private UserRepository userRepository;
    private RateRepository rateRepository;
    private ProductRepository productRepository;

    public ResultService(UserRepository userRepositor, ResultRepository resultRepository, RateRepository rateRepository, ProductRepository productRepository) {
        this.userRepository = userRepositor;
        this.resultRepository = resultRepository;
        this.rateRepository = rateRepository;
        this.productRepository = productRepository;
    }
    // todo powywalac getbyid


    public ApiResponse rateResult(Long userId, RateTO rateTO) {

        Result result = resultRepository
                .findById(rateTO.getResultId()).orElseThrow(() -> new BadRequestException("Result doesn't exist"));

        if (!userId.equals(result.getReceiver().getId())) {
            throw new BadRequestException("User is not receiver of role");
        }

        Rate rate = new Rate()
                .setRater(userRepository.findById(userId).orElseThrow(() -> new AppException("User not in db")))
                .setRateName(rateTO.getRateName())
                .setComment(rateTO.getComment())
                .setResult(result);

        rateRepository.save(rate);

        return new ApiResponse("Result rated succesfully");
    }


    public Set<RateTO> getUserRates(Long id) {
        return ServerToClientDataConverter.mapRates(rateRepository.findAllByResult_Sender_Id(id)) ;

    }

    public Set<ResultTO> getEditionProductsToSendForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllBySender_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<ResultTO> getEditionProductsToReceiveForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllByReceiver_IdAndEdition_Id(userId, editionId)) ;
    }
}
