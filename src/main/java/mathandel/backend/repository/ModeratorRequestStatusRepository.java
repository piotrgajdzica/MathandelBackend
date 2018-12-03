package mathandel.backend.repository;

import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeratorRequestStatusRepository extends JpaRepository<ModeratorRequestStatus, Long> {
    Optional<ModeratorRequestStatus> findByName(ModeratorRequestStatusName name);
    Boolean existsByName(ModeratorRequestStatusName name);
}
