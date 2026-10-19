package br.com.acta.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AtivarRequestDTO(
        @Schema(description = "Código alfanumérico de 6 caracteres recebido por e-mail", example = "A1B2C3")
        @NotBlank(message = "O token do convite é obrigatório")
        @Pattern(regexp = "[A-Z0-9]{6}", message = "O código do convite deve conter 6 caracteres entre A-Z e 0-9")
        String token
) {}