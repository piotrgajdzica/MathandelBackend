package mathandel.backend.component;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.server.Edition;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.service.CalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static mathandel.backend.model.server.enums.EditionStatusName.OPENED;

@Component
public class CloseEditionScheduler {

    private final EditionRepository editionRepository;
    private final CalcService calcService;
    private static final Logger log = LoggerFactory.getLogger(CloseEditionScheduler.class);

    public CloseEditionScheduler(EditionRepository editionRepository, CalcService calcService) {
        this.editionRepository = editionRepository;
        this.calcService = calcService;

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeEditionAutomatically() {
        Set<Edition> editions = editionRepository.
                findAllByEndDateBeforeAndEditionStatusType_EditionStatusName(LocalDate.now(), OPENED);

        editions.forEach(edition -> {
            try {
                calcService.calculateResults(edition);
            } catch (AppException exc) {
                exc.printStackTrace();
            }
        });
        log.info("Editions " + editions.stream().map(Edition::getId).collect(Collectors.toList()).toString() + " closed");
    }

}
