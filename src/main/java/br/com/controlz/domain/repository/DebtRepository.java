package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

	Optional<Debt> findById(Long id);
}
