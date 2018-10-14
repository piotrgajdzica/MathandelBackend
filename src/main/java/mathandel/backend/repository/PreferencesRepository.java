package mathandel.backend.repository;

import mathandel.backend.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PreferencesRepository extends JpaRepository<Preference, Long> {
    Preference getByWantProduct_IdAndHaveProduct_Id(Long wantProductId, Long haveProductId);
    Preference getByWantProduct_IdAndDefinedGroup_Id(Long wantProductId, Long defineProduct);

    Set<Preference> findAllByUser_IdAndEdition_Id(Long userId, Long editionId);
}
