package mathandel.backend.repository;

import mathandel.backend.model.server.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Set<Rate> findAllByResult_Sender_Id(Long id);
}