package mathandel.backend.repository;

import mathandel.backend.model.server.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long>{
    Boolean existsByName(String name);
    Optional<Edition> findByName(String name);
    Set<Edition> findAllByEndDate(LocalDate endDate);
}
