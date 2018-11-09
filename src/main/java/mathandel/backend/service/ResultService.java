package mathandel.backend.service;

import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ResultTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.*;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ResultService {

    private ResultRepository resultRepository;
    private UserRepository userRepository;
    private EditionRepository editionRepository;

    public ResultService(UserRepository userRepository, ResultRepository resultRepository, TransactionRateRepository transactionRateRepository, ProductRepository productRepository, EditionRepository editionRepository) {
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.editionRepository = editionRepository;
    }

    // todo close edition
    public Set<ResultTO> getEditionProductsToSendForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllBySender_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<ResultTO> getEditionProductsToReceiveForUser(Long userId, Long editionId) {
        return ServerToClientDataConverter.mapResults(resultRepository.findAllByReceiver_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<Result> getEditionResults(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not in db"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if(!edition.getModerators().contains(user)){
            throw  new BadRequestException("User is not authorised to close this edition");
        }

        if(!edition.getEditionStatusType().getEditionStatusName().equals(EditionStatusName.FINISHED)){
            throw new BadRequestException("Edition is not finished. Please finish edition first");
        }

        return resultRepository.findAllByEdition_Id(editionId);
    }
}
