package br.com.acta.repository;

import br.com.acta.entity.core.contato.EmailColaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailColaboradorRepository extends JpaRepository<EmailColaborador, Long> {
    boolean existsByContatoIgnoreCase(String contato);
}
