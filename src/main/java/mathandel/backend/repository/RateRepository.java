package mathandel.backend.repository;

import mathandel.backend.model.server.Rate;
import mathandel.backend.model.server.enums.RateName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long>
{
    Optional<Rate> findByName(RateName name);
}
