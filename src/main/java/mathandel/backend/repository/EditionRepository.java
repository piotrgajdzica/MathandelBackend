package mathandel.backend.repository;

import mathandel.backend.model.server.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long>{
    Boolean existsByName(String name);
}
