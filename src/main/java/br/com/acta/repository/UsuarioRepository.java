package br.com.acta.repository;

import br.com.acta.common.handler.exception.ModelNotFoundException;
import br.com.acta.entity.core.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailLoginIgnoreCase(String emailLogin);
    boolean existsByFirebaseUid(String firebaseUid);

    // busca usuário pelo firebaseUid e já carrega empresa e colaborador
    @Query("""
        SELECT u FROM Usuario u
        JOIN FETCH u.empresa
        LEFT JOIN FETCH u.colaborador
        WHERE u.firebaseUid = :firebaseUid""")
    Optional<Usuario> findByFirebaseUid(String firebaseUid);

    @Query("""
        SELECT u FROM Usuario u
        JOIN FETCH u.empresa
        LEFT JOIN FETCH u.colaborador
        WHERE LOWER(u.emailLogin) = LOWER(:emailLogin)""")
    Optional<Usuario> findByEmailLoginIgnoreCase(String emailLogin);

    boolean existsByIdAndEmpresaId(Long id, Long empresaId);
    Optional<Usuario> findByIdAndEmpresaId(Long id, Long empresaId);

    default Usuario getEntity(Long id, Long empresaId) {
        return findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ModelNotFoundException("Usuário", id));
    }
}
