package mathandel.backend.repository;

import mathandel.backend.model.server.Item;
import mathandel.backend.model.server.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByHaveItem_Id(Long haveItemId);
    Set<Preference> findAllByUser_IdAndEdition_Id(Long user_id, Long edition_id);
    Set<Preference> findAllByEdition_Id(Long edition_id);
    Set<Preference> findAllByWantedItemsContains(Set<Item> wantedItems);
    void deleteAllByEdition_Id(Long edition_id);
}
