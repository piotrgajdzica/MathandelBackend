package mathandel.backend.repository;

import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.enums.EditionStatusName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditionStatusTypeRepository extends JpaRepository<EditionStatusType, Long> {
    EditionStatusType findByEditionStatusName(EditionStatusName editionStatusName);
}
