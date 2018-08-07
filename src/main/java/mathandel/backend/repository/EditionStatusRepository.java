package mathandel.backend.repository;

import mathandel.backend.model.EditionStatus;
import mathandel.backend.model.EditionStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditionStatusRepository extends JpaRepository<EditionStatus, Long> {

    EditionStatus findByEditionStatusName(EditionStatusName editionStatusName);
}
