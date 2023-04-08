package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.FinancialHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialHistoryRepository extends JpaRepository<FinancialHistory, Long> {

	List<FinancialHistory> findByRegisterId(Long registerId);

	Optional<FinancialHistory> findByRegisterIdAndFinancialHistoryId(Long registerId, Long historyId);
}
