package mathandel.backend.repository;

import mathandel.backend.model.Edition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditionRepository extends JpaRepository<Edition, Long>{
    Boolean existsByName(String name);
}
