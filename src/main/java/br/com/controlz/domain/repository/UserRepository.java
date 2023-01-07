package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAll();

	Optional<User> findByEmail(String email);

	Optional<User> findByName(String name);

}