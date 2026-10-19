package br.com.acta.common.config.firebase;

import br.com.acta.common.handler.exception.FirebaseAccessRevokedException;
import br.com.acta.common.handler.exception.FirebaseIdTokenException;
import br.com.acta.entity.core.Colaborador;
import br.com.acta.entity.core.Empresa;
import br.com.acta.entity.core.Usuario;
import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.mapper.AuthMapper;
import br.com.acta.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
public final class FirebaseUtils {
    private static final String BEARER = "Bearer ";
    private final FirebaseAuth firebaseAuth;
    private final UsuarioRepository repo;
    private final AuthMapper mapper;

    public String extrairToken(HttpServletRequest req){
        String authorization = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isBlank()) return null;
        if (!authorization.startsWith(BEARER)) throw new FirebaseIdTokenException();

        String token = authorization.substring(BEARER.length()).trim();
        if (token.isEmpty()) throw new FirebaseIdTokenException();

        return token;
    }

    public FirebaseToken verificarIdToken(String token) {
        try {
            // verifica se token é válido, ignorando usuário revogados e com firebase desabilitado
            return firebaseAuth.verifyIdToken(token, true);
        } catch (FirebaseAuthException fae) {
            throw new FirebaseIdTokenException();
        }
    }

    public UsuarioAutenticado extrairUsuario(FirebaseToken token){
        Usuario usuario = getEntity(token.getUid());
        validarAcesso(usuario);

        return mapper.toUsuarioAutenticado(usuario);
    }

    // cria objeto de autenticação, já autenticado
    public void autenticar(HttpServletRequest req, Principal principal, List<? extends GrantedAuthority> authorities){
        UsernamePasswordAuthenticationToken auth = UsernamePasswordAuthenticationToken.authenticated(principal, null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req)); // detalhes da requisição (ip, sessão, etc.)

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    public void validarAcesso(Usuario usuario) {
        if (usuario.getStatus() != StatusGeral.ATIVO) throw new FirebaseAccessRevokedException();

        Empresa empresa = usuario.getEmpresa();
        if (empresa.getStatus() != StatusGeral.ATIVO) throw new FirebaseAccessRevokedException();

        Colaborador colaborador = usuario.getColaborador();
        if (colaborador != null && colaborador.getStatus() != StatusGeral.ATIVO) throw new FirebaseAccessRevokedException();
    }

    private Usuario getEntity(String firebaseUid) {
        return repo.findByFirebaseUid(firebaseUid)
                .orElseThrow(FirebaseAccessRevokedException::new);
    }
}
