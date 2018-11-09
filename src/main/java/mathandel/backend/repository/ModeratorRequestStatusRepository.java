package mathandel.backend.repository;

import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.ModeratorRequestStatusName;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ModeratorRequestStatusRepository extends JpaRepository<ModeratorRequestStatus, Long> {
    ModeratorRequestStatus findByName(ModeratorRequestStatusName name);
}
