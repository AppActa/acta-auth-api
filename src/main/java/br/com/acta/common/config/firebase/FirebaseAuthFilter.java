package br.com.acta.common.config.firebase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseToken;

import br.com.acta.common.handler.ErroResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class FirebaseAuthFilter extends OncePerRequestFilter {
    private static final String AUTH = "/api/v1/auth/ativar";
    private static final String ONBOARDING = "/api/v1/onboarding/inicio";
    private static final String ONBOARDING_EMPRESA = "/api/v1/onboarding/gestor";
    private static final String ME = "/api/v1/me";
    private final FirebaseUtils utils;
    private final ObjectMapper mapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return "/api/v1/health".equals(request.getServletPath()) || "/images/acta-logo.png".equals(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = utils.extrairToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            FirebaseToken idToken = utils.verificarIdToken(token);

            if (ehRotaIdentidadeFirebase(request)) {
                Principal principal = new FirebaseIdentity(idToken.getUid(), idToken.getEmail(), idToken.isEmailVerified());
                utils.autenticar(request, principal, List.of(new SimpleGrantedAuthority("ROLE_FIREBASE")));
            } else {
                UsuarioAutenticado usuario = utils.extrairUsuario(idToken);
                utils.autenticar(request, usuario, usuario.authorities());
            }

            filterChain.doFilter(request, response);
        } catch (BadCredentialsException bce) {
            SecurityContextHolder.clearContext();
            responderErro(response, HttpStatus.UNAUTHORIZED, "O ID Token do Firebase não existe ou está inválido");
        } catch (AccessDeniedException ade) {
            SecurityContextHolder.clearContext();
            responderErro(response, HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }

    public record FirebaseIdentity(String firebaseUid, String email, boolean emailVerificado) implements Principal {
        @Override
        public String getName() {
            return firebaseUid;
        }
    }

    private boolean ehRotaIdentidadeFirebase(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        return (path.equals(AUTH) && "POST".equalsIgnoreCase(method))
                || (path.equals(ONBOARDING) && "POST".equalsIgnoreCase(method))
                || (path.equals(ONBOARDING_EMPRESA) && "POST".equalsIgnoreCase(method))
                || (path.equals(ME) && "GET".equalsIgnoreCase(method));
    }

    private void responderErro(HttpServletResponse resp, HttpStatus status, String mensagem) throws IOException {
        resp.setStatus(status.value());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        mapper.writeValue(resp.getOutputStream(), new ErroResponse(List.of(mensagem), status.value()));
    }
}