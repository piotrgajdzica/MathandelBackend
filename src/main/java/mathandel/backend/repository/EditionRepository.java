package mathandel.backend.repository;

import mathandel.backend.model.server.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long>{
    Boolean existsByName(String name);
    Optional<Edition> findByName(String name);
}
