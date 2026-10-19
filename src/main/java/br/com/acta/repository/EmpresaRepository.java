package br.com.acta.repository;

import br.com.acta.common.handler.exception.ModelNotFoundException;
import br.com.acta.entity.core.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    default Empresa getEntity(Long id, Long idEmpresaAtual) {
        if (!id.equals(idEmpresaAtual)) {
            throw new ModelNotFoundException("Empresa", id);
        }
        return findById(id).orElseThrow(() -> new ModelNotFoundException("Empresa", id));
    }
}
