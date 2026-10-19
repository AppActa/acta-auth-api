package br.com.acta.service;

import br.com.acta.dto.health.HealthResponseDTO;
import br.com.acta.entity.enums.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final JdbcTemplate jdbcTemplate;
    private final String mensagemSucesso = "O CATO verificou: a API e o banco estão funcionando!";
    private final String mensagemErro = "O CATO encontrou um problema na conexão com o banco";

    public HealthResponseDTO verificar() {
        try {
            testarBanco();
            return new HealthResponseDTO(HealthStatus.UP, HealthStatus.UP, mensagemSucesso, OffsetDateTime.now());
        } catch (Exception e) {
            return new HealthResponseDTO(HealthStatus.DOWN, HealthStatus.DOWN, mensagemErro, OffsetDateTime.now());
        }
    }

    private void testarBanco() {
        Integer resultado = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        if (resultado == null || resultado != 1) {
            throw new RuntimeException("Erro ao testar o banco");
        }
    }
}