package mathandel.backend.component;

import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.EditionStatusTypeRepository;
import mathandel.backend.service.EditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EditionClosingUtil {


    private final EditionRepository editionRepository;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private static final Logger log = LoggerFactory.getLogger(EditionClosingUtil.class);


    public EditionClosingUtil(EditionRepository editionRepository, EditionStatusTypeRepository editionStatusTypeRepository, EditionService editionService) {
        this.editionRepository = editionRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void closeEditionAutomatically() {
        LocalDate today = LocalDate.now();
        Set<Edition> editions = editionRepository.findAllByEndDate(today);
        EditionStatusType editionStatusType = editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.CLOSED);

        for (Edition edition :
                editions) {
            edition.setEditionStatusType(editionStatusType);
        }

        editionRepository.saveAll(editions);
        log.info("Editions " + editions.stream().map(Edition::getId).collect(Collectors.toList()).toString() + " closed");

    }

}
