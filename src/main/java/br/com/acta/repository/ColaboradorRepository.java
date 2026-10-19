package br.com.acta.repository;

import br.com.acta.common.handler.exception.ModelNotFoundException;
import br.com.acta.entity.core.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByIdAndEmpresaId(Long id, Long idEmpresa);
    Optional<Colaborador> findByIdAndEmpresaId(Long id, Long idEmpresa);
    Optional<Colaborador> findByUsuarioIdAndEmpresaId(Long usuarioId, Long idEmpresa);

    default Colaborador getEntity(Long id, Long idEmpresa) {
        return findByIdAndEmpresaId(id, idEmpresa)
                .orElseThrow(() -> new ModelNotFoundException("Colaborador", id));
    }
}