package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ResultTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.User;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.RateRepository;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static mathandel.backend.model.server.enums.EditionStatusName.FINISHED;
import static mathandel.backend.utils.ServerToClientDataConverter.mapResults;
import static mathandel.backend.utils.ServerToClientDataConverter.mapResultsToReceive;
import static mathandel.backend.utils.ServerToClientDataConverter.mapResultsToSend;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;

    public ResultService(UserRepository userRepository, ResultRepository resultRepository, EditionRepository editionRepository, RateRepository rateRepository) {
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.editionRepository = editionRepository;
    }

    public Set<ResultTO> getEditionProductsToSendForUser(Long userId, Long editionId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));

        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
        if(!edition.getEditionStatusType().getEditionStatusName().equals(FINISHED)) {
            throw new BadRequestException("Edition is not finished yet");
        }

        return mapResultsToSend(resultRepository.findAllBySender_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<ResultTO> getEditionProductsToReceiveForUser(Long userId, Long editionId) {
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));

        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
        if(!edition.getEditionStatusType().getEditionStatusName().equals(FINISHED)) {
            throw new BadRequestException("Edition is not finished yet");
        }
        return mapResultsToReceive(resultRepository.findAllByReceiver_IdAndEdition_Id(userId, editionId)) ;
    }

    public Set<ResultTO> getEditionResults(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not in db"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if(!edition.getModerators().contains(user)){
            throw  new BadRequestException("User is not moderator of this edition");
        }

        if(!edition.getEditionStatusType().getEditionStatusName().equals(FINISHED)){
            throw new BadRequestException("Edition is not finished. Please finish edition first");
        }

        return mapResults(resultRepository.findAllByEdition_Id(editionId));
    }
}
