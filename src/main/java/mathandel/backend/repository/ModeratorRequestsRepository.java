package mathandel.backend.repository;

import mathandel.backend.model.server.ModeratorRequest;
import mathandel.backend.model.server.ModeratorRequestStatusName;
import mathandel.backend.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeratorRequestsRepository extends JpaRepository<ModeratorRequest, Long> {

    Boolean existsByUser(User user);

    //todo change to set
    List<ModeratorRequest> findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName moderatorRequestStatusName);

    Optional<ModeratorRequest> findModeratorRequestsByUser_Id(Long id);

}
