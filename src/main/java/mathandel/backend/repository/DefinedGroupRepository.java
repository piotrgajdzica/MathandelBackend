package mathandel.backend.repository;

import mathandel.backend.model.server.DefinedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DefinedGroupRepository extends JpaRepository<DefinedGroup, Long> {
    Boolean existsByNameAndUser_IdAndEdition_Id(String name, Long user_id, Long edition_id);
    Set<DefinedGroup> findByUser_IdAndEdition_Id(Long user_id, Long edition_id);
}
