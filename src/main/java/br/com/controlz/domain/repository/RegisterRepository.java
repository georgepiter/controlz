package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

	@Query("SELECT d FROM Register d WHERE d.userId = :userId AND d.registerId = :registerId")
	Optional<Register> findByRegisterIdAndUserId(@Param("userId") Long userId, @Param("registerId") Long registerId);

	Optional<Register> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

}
