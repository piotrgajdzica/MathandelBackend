package mathandel.backend.repository;

import mathandel.backend.model.server.Item;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Set<Item> findByUser_IdAndEditionIsNull(Long user_id);
    Set<Item> findByEdition_IdAndUser_IdNot(Long edition_id, Long user_id);
    Set<Item> findAllByEdition_Id(Long edition_id);
    Set<Item> findByEdition_IdAndUser_Id(Long edition_id, Long user_id);
    Set<Item> findByName(String name);
    Set<Item> findAllByIdIn(Collection<Long> id);
}
