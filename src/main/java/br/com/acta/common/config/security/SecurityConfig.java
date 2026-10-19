package br.com.acta.common.config.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import br.com.acta.common.config.firebase.FirebaseAuthFilter;
import br.com.acta.common.config.firebase.FirebaseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import br.com.acta.common.handler.ErroResponse;
import br.com.acta.mapper.AuthMapper;
import br.com.acta.repository.UsuarioRepository;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Configuration
@Profile("!test")
@EnableWebSecurity
// tambem permite usar @PreAuthorize nos services quando uma regra for mais especifica que a rota
@EnableMethodSecurity
public class SecurityConfig {
    @Bean(destroyMethod = "delete")
    public FirebaseApp app(@Value("${acta.firebase.project-id}") String idProjeto) throws IOException {
        // o sdk usa as credenciais do servidor, nunca o token do app
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setProjectId(idProjeto)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth auth(FirebaseApp app) {
        return FirebaseAuth.getInstance(app);
    }

    @Bean
    public FirebaseUtils utils(FirebaseAuth auth, UsuarioRepository repo, AuthMapper mapper) {
        return new FirebaseUtils(auth, repo, mapper);
    }

    @Bean
    public FirebaseAuthFilter filter(FirebaseUtils utils, ObjectMapper mapper) {
        return new FirebaseAuthFilter(utils, mapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, FirebaseAuthFilter filter, ObjectMapper mapper){
        http
                .csrf(AbstractHttpConfigurer::disable) // desabilita o csrf já que a api usa bearer e não cookies
                .cors(Customizer.withDefaults()) // configura o cors

                // desliga a tela de login e o logout do spring
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // manda os erros como erroresponse
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, resp, e) ->
                                erro(mapper, resp, HttpStatus.UNAUTHORIZED, "O ID Token do Firebase não existe ou está inválido"))
                        .accessDeniedHandler((req, resp, e) ->
                                erro(mapper, resp, HttpStatus.FORBIDDEN, "Acesso negado"))
                )
                .authorizeHttpRequests(a -> a
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        // health e documentacao precisam abrir sem token
                        .requestMatchers(HttpMethod.GET, "/api/v1/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/acta-logo.png").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // filtro ainda nao procura usuario no banco, so valida a identidade firebase
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/ativar").hasAuthority("ROLE_FIREBASE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/onboarding/inicio").hasAuthority("ROLE_FIREBASE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/onboarding/gestor").hasAuthority("ROLE_FIREBASE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/me").hasAuthority("ROLE_FIREBASE")

                        // o restante so segue depois que o filtro montou o usuario autenticado
                        .anyRequest().authenticated()
                )
                // o filtro precisa rodar antes do spring decidir se a rota foi autenticada
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void erro(ObjectMapper mapper, HttpServletResponse resp, HttpStatus status, String msg) throws IOException {
        resp.setStatus(status.value());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        mapper.writeValue(resp.getOutputStream(), new ErroResponse(List.of(msg), status.value()));
    }
}
