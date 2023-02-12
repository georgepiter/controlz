package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

	Optional<Debt> findByDebtId(Long debtId);

	List<Debt> findAll();

	List<Debt> findByStatusAndRegisterId(Integer status, Long registerId);

	Optional<Debt> findByDebtIdAndRegisterId(Long debtId, Long registerId);

	List<Debt> findByStatus(Integer value);

	@Query("SELECT d FROM Debt d WHERE d.dueDate BETWEEN :start AND :end AND d.registerId = :registerId")
	List<Debt> findByDueDateAndRegisterId(@Param("start") LocalDate start, @Param("end") LocalDate end,@Param("registerId") Long registerId);

	@Query("SELECT d FROM Debt d WHERE d.dueDate BETWEEN :start AND :end")
	List<Debt> findByDueDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}


