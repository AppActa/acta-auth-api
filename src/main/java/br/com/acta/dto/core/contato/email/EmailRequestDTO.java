package br.com.acta.dto.core.contato.email;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmailRequestDTO(
        @Schema(description = "E-mail do usuário", example = SwaggerRequestExamples.EMAIL, maxLength = 254)
        @Email(message = "{validation.email.invalid}")
        @NotBlank(message = "{validation.email.notblank}")
        @Size(max = 254, message = "{validation.email.size}")
        String email,

        @Schema(description = "Indica se o e-mail é principal", example = SwaggerRequestExamples.PRINCIPAL)
        @NotNull(message = "{validation.email.principal.notnull}")
        Boolean principal
) {
}
