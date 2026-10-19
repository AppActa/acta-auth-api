package br.com.acta.common.client;

import br.com.acta.common.handler.exception.BusinessRuleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class BrevoClient {
    private static final RestClient REST_CLIENT = RestClient.create();

    @Value("${BREVO_API_KEY:}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL:}")
    private String senderEmail;

    @Value("${EXTERNAL_URL:http://localhost:8080}")
    private String url;

    public void enviarConvite(String email, String nome, OffsetDateTime expiraEm, String token) {
        String html = carregarTemplate()
                .replace("{{nome}}", HtmlUtils.htmlEscape(nome))
                .replace("{{expiraEm}}", expiraEm.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")))
                .replace("{{logoUrl}}", HtmlUtils.htmlEscape(url.replaceAll("/+$", "") + "/images/acta-logo.png"))
                .replace("{{codigo}}", HtmlUtils.htmlEscape(token));

        enviarEmail(email, html);
    }

    private void enviarEmail(String email, String html) {
        Map<String, Object> mensagem = montarMensagem(email, html);

        try {
            REST_CLIENT.post()
                    .uri("https://api.brevo.com/v3/smtp/email").contentType(MediaType.APPLICATION_JSON)
                    .header("api-key", apiKey)
                    .body(mensagem).retrieve().toBodilessEntity();
        } catch (RestClientException rce) {
            throw new BusinessRuleException("Não foi possível enviar o convite por e-mail");
        }
    }

    private Map<String, Object> montarMensagem(String email, String html) {
        return Map.of(
                "sender", Map.of("name", "ACTA", "email", senderEmail),
                "to", List.of(Map.of("email", email)),
                "subject", "Convite para acessar o ACTA",
                "htmlContent", html
        );
    }

    private String carregarTemplate() {
        try (InputStream input = new ClassPathResource("templates/email/" + "convite-usuario.html").getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            throw new BusinessRuleException("Não foi possível carregar o e-mail de convite");
        }
    }
}