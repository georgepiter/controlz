package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

	Optional<Register> findById(Long id);

	List<Register> findAll();

	Optional<Register> findByName(String name);
}
