package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.security.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

	Optional<Email> findByEmailAndEmailStatus(String email, Integer status);

	Optional<Email> findByEmail(String email);
}
