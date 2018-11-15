package mathandel.backend.repository;

import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import mathandel.backend.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ModeratorRequestsRepository extends JpaRepository<ModeratorRequest, Long> {
    Boolean existsByUser(User user);
    Set<ModeratorRequest> findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName moderatorRequestStatusName);
    Optional<ModeratorRequest> findModeratorRequestsByUser_Id(Long id);
    Optional<ModeratorRequest> findAllByUser_Id(Long userId);
}
