package mathandel.backend.repository;

import mathandel.backend.model.server.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Set<Result> findAllBySender_IdAndEdition_Id(Long sender_id, Long edition_id);

    Set<Result> findAllByReceiver_IdAndEdition_Id(Long receiver_id, Long edition_id);

}
