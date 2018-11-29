package mathandel.backend.repository;

import mathandel.backend.model.server.DefinedGroup;
import mathandel.backend.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DefinedGroupRepository extends JpaRepository<DefinedGroup, Long> {
    Boolean existsByNameAndUser_IdAndEdition_Id(String name, Long user_id, Long edition_id);
    Set<DefinedGroup> findByUser_IdAndEdition_Id(Long user_id, Long edition_id);
    Set<DefinedGroup> findAllByEdition_Id(Long edition_id);
    void deleteAllByEdition_Id(Long edition_id);
    Set<DefinedGroup> findAllByNameIn(Collection<@NotBlank @Size(max = 100) String> name);
    DefinedGroup findByNameAndUser(String name, User user);
}
