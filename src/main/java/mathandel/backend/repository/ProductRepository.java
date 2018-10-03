package mathandel.backend.repository;

import mathandel.backend.model.Edition;
import mathandel.backend.model.Product;
import mathandel.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findByUserAndEditionIsNull(User user);
    Set<Product> findByEditionAndUser(Edition edition, User user);
    Set<Product> findByEditionAndUserNot(Edition edition, User user);
}
