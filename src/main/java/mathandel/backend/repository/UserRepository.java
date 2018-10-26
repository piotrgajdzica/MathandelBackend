package mathandel.backend.repository;

import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByFacebookId(String facebookId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByFacebookId(String facebookId);
    Optional<User> findByRolesContains(Role role);
}
