package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

	Optional<Register> findByUserIdAndRegisterId(Long userId, Long registerId);

	Optional<Register> findByUserId(Long userId);

}
