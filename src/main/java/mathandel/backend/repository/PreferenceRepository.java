package mathandel.backend.repository;

import mathandel.backend.model.server.Preference;
import mathandel.backend.model.server.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Preference findByHaveProduct_Id(Long haveProductId);
    Set<Preference> findAllByUser_IdAndEdition_Id(Long user_id, Long edition_id);
    Set<Preference> findAllByEdition_Id(Long edition_id);
    Set<Preference> findAllByWantedProductsContains(Set<Product> wantedProducts);
}
