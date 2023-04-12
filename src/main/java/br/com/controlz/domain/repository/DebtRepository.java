package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

	Optional<Debt> findByDebtId(Long debtId);

	Optional<Debt> findByDebtIdAndRegisterId(Long debtId, Long registerId);

	List<Debt> findByStatus(Integer value);

	@Query("SELECT d FROM Debt d WHERE d.dueDate BETWEEN :start AND :end AND d.registerId = :registerId")
	List<Debt> findByDueDateAndRegisterId(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("registerId") Long registerId);

	@Query("SELECT d FROM Debt d WHERE d.dueDate BETWEEN :start AND :end")
	List<Debt> findByDueDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

	@Query("SELECT d FROM Debt d WHERE d.register.registerId = :registerId AND FUNCTION('YEAR', d.dueDate) = FUNCTION('YEAR', :monthDate) AND FUNCTION('MONTH', d.dueDate) = FUNCTION('MONTH', :monthDate)")
	List<Debt> findByRegisterIdAndDueDate(@Param("registerId") Long registerId, @Param("monthDate") LocalDate monthDate);

	@Modifying
	@Transactional
	@Query("UPDATE Debt d SET d.categoryId = null WHERE d.categoryId = :categoryId")
	void updateCategoryIdOnDebt(@Param("categoryId") Long categoryId);

}


