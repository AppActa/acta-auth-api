package br.com.acta.dto.onboarding;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record OnboardingInicioRequestDTO(
        @Schema(description = "Dados do primeiro gestor da empresa")
        @NotNull(message = "{validation.onboarding.gestor.notnull}")
        @Valid
        OnboardingRequestDTO.Gestor gestor,

        @Schema(description = "CNPJ da empresa que o gestor deseja acessar", example = SwaggerRequestExamples.CNPJ)
        @NotBlank(message = "{validation.empresa.cnpj.notblank}")
        @Size(min = 14, max = 14, message = "{validation.empresa.cnpj.size}")
        @CNPJ(message = "{validation.empresa.cnpj.invalid}")
        String cnpj
) {
}