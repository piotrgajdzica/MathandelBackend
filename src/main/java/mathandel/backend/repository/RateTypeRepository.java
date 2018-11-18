package mathandel.backend.repository;

import mathandel.backend.model.server.RateType;
import mathandel.backend.model.server.enums.RateTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RateTypeRepository extends JpaRepository<RateType, Long> {
    Optional<RateType> findByName(RateTypeName name);
}
