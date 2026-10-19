package br.com.acta.service;

import br.com.acta.common.config.firebase.UsuarioAutenticado;
import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.handler.exception.FirebaseAccessRevokedException;
import br.com.acta.mapper.AuthMapper;
import br.com.acta.entity.enums.EstadoPerfil;
import br.com.acta.dto.auth.MeResponseDTO;
import br.com.acta.repository.ColaboradorRepository;
import br.com.acta.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository repo;
    private final ColaboradorRepository colaboradorRepo;
    private final AuthMapper authMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public UsuarioAutenticado atual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UsuarioAutenticado usuario) || !auth.isAuthenticated())
            throw new AuthenticationCredentialsNotFoundException("Usuário não autenticado");

        return usuario;
    }

    @Transactional(readOnly = true)
    public MeResponseDTO buscarParaMe(FirebaseIdentity identity) {
        if (identity == null) throw new FirebaseAccessRevokedException();
        return repo.findByFirebaseUid(identity.firebaseUid())
                .map(authMapper::toUsuarioAutenticado)
                .map(authMapper::toMeResponse)
                .orElseGet(() -> new MeResponseDTO(identity.firebaseUid(), null, null, null, null, identity.email(), null, null, false, null, EstadoPerfil.CADASTRO_NAO_INICIADO));
    }

    // só executa se existir uma transação aberta
    @Transactional(propagation = Propagation.MANDATORY)
    public void configurarUsuarioAtual() {
        UsuarioAutenticado usuario = atual();

        entityManager.createNativeQuery("SELECT set_config('app.current_user_id', :id, true)")
                .setParameter("id", usuario.idUsuario().toString())
                .getSingleResult();
    }

    public boolean isColaboradorEmpresa(Long idColaborador) {
        return colaboradorRepo.existsByIdAndEmpresaId(idColaborador, atual().idEmpresa());
    }

    public boolean isUsuarioByIdEmpresa(Long idEmpresa) {
        return repo.existsByIdAndEmpresaId(atual().idUsuario(), idEmpresa);
    }
}
