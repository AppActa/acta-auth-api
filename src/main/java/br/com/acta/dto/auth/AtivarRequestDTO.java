package br.com.acta.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AtivarRequestDTO(
        @Schema(description = "Código alfanumérico de 6 caracteres recebido por e-mail", example = "A1B2C3")
        @NotBlank(message = "{validation.auth.token.notblank}")
        @Pattern(regexp = "[A-Z0-9]{6}", message = "{validation.auth.token.pattern}")
        String token
) {}
