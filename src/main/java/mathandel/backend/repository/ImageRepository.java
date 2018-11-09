package mathandel.backend.repository;

import mathandel.backend.model.server.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Boolean existsByName(String name);
    Optional<Image> findByName(String name);
}
