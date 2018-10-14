package mathandel.backend.repository;

import mathandel.backend.model.server.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findByUser_IdAndEditionIsNull(Long user_id);
    Set<Product> findByEdition_IdAndUser_IdNot(Long edition_id, Long user_id);
    Set<Product> findByEdition_IdAndUser_Id(Long edition_id, Long user_id);
}
