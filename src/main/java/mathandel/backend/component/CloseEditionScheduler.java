package mathandel.backend.component;

import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static mathandel.backend.model.server.enums.EditionStatusName.CLOSED;
import static mathandel.backend.model.server.enums.EditionStatusName.OPENED;

@Component
public class CloseEditionScheduler {

    private final EditionRepository editionRepository;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private static final Logger log = LoggerFactory.getLogger(CloseEditionScheduler.class);

    public CloseEditionScheduler(EditionRepository editionRepository, EditionStatusTypeRepository editionStatusTypeRepository) {
        this.editionRepository = editionRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeEditionAutomatically() {
        Set<Edition> editions = editionRepository.
                findAllByEndDateBeforeAndEditionStatusType_EditionStatusName(LocalDate.now(), OPENED);
        EditionStatusType editionStatusType = editionStatusTypeRepository.findByEditionStatusName(CLOSED);

        editions.forEach(edition -> edition.setEditionStatusType(editionStatusType));

        editionRepository.saveAll(editions);
        log.info("Editions " + editions.stream().map(Edition::getId).collect(Collectors.toList()).toString() + " closed");
    }
}
