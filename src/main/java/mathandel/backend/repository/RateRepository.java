package mathandel.backend.repository;

import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RateRepository extends JpaRepository<Rate, Long> {
    Boolean existsByResult(Result result);
    Optional<Rate> findByResult(Result result);
    Set<Rate> findAllByResult_Sender_Id(Long id);
}