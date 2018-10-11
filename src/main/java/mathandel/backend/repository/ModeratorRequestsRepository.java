package mathandel.backend.repository;

import mathandel.backend.model.ModeratorRequest;
import mathandel.backend.model.ModeratorRequestStatusName;
import mathandel.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeratorRequestsRepository extends JpaRepository<ModeratorRequest, Long> {

    // save juz jest
    Optional<ModeratorRequest> findByUser(User user);

    List<ModeratorRequest> findAllByModeratorRequestStatus_Name(ModeratorRequestStatusName moderatorRequestStatusName);

    Optional<ModeratorRequest> findModeratorRequestsByUser_Id(Long id);






}
