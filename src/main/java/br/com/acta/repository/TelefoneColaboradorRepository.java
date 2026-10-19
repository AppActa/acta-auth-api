package br.com.acta.repository;

import br.com.acta.entity.core.contato.TelefoneColaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneColaboradorRepository extends JpaRepository<TelefoneColaborador, Long> {
    boolean existsByContatoIgnoreCase(String contato);
}
