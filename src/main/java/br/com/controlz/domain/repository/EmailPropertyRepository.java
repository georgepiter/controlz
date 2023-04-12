package br.com.controlz.domain.repository;

import br.com.controlz.domain.entity.security.EmailProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailPropertyRepository extends JpaRepository<EmailProperty, Long> {

}
