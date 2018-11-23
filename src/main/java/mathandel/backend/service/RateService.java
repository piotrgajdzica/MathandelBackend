package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.RateTO;
import mathandel.backend.model.client.RateTypeTO;
import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.Result;
import mathandel.backend.model.server.User;
import mathandel.backend.repository.RateRepository;
import mathandel.backend.repository.RateTypeRepository;
import mathandel.backend.repository.ResultRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapRate;
import static mathandel.backend.utils.ServerToClientDataConverter.mapRateTypes;

@Service
public class RateService {

    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final RateTypeRepository rateTypeRepository;

    public RateService(ResultRepository resultRepository, UserRepository userRepository, RateRepository rateRepository, RateTypeRepository rateTypeRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.rateRepository = rateRepository;
        this.rateTypeRepository = rateTypeRepository;
    }

    public RateTO rateResult(Long userId, Long resultId, RateTO rateTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Result result = resultRepository.findById(resultId).orElseThrow(() -> new ResourceNotFoundException("Result", "id", resultId));

        if (!user.getId().equals(result.getReceiver().getId())) {
            throw new BadRequestException("User is not receiver of this result");
        }

        Rate rate = rateRepository.findByResult(result).orElseGet(Rate::new)
                .setRater(user)
                .setRate(rateTO.getRate())
                .setComment(rateTO.getComment())
                .setResult(result);

        result.setRate(rate);

        return mapRate(rateRepository.save(rate));
    }

    public Set<RateTypeTO> getRateTypes() {
        return mapRateTypes(new HashSet<>(rateTypeRepository.findAll()));
    }
}
