package mathandel.backend.repository;

import mathandel.backend.model.server.TransactionRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TransactionRateRepository extends JpaRepository<TransactionRate, Long> {
    Set<TransactionRate> findAllByResult_Sender_Id(Long id);
}