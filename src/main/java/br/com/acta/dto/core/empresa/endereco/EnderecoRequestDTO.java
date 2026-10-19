package br.com.acta.dto.core.empresa.endereco;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import br.com.acta.entity.enums.UF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
        @Schema(description = "CEP do endereço", example = SwaggerRequestExamples.CEP)
        @NotBlank(message = "{validation.cep.notblank}")
        @Pattern(regexp = "\\d{8}", message = "{validation.cep.pattern}")
        @Size(min = 8, max = 8, message = "{validation.cep.size}")
        String cep,

        @Schema(description = "UF do endereço", example = SwaggerRequestExamples.UF, implementation = UF.class)
        @NotNull(message = "{validation.uf.notnull}")
        UF uf,

        @Schema(description = "Cidade do endereço", example = SwaggerRequestExamples.CIDADE)
        @NotBlank(message = "{validation.cidade.notblank}")
        @Size(max = 100, message = "{validation.cidade.size}")
        String cidade,

        @Schema(description = "Bairro do endereço", example = SwaggerRequestExamples.BAIRRO)
        @NotBlank(message = "{validation.bairro.notblank}")
        @Size(max = 100, message = "{validation.bairro.size}")
        String bairro,

        @Schema(description = "Logradouro do endereço", example = SwaggerRequestExamples.LOGRADOURO)
        @NotBlank(message = "{validation.logradouro.notblank}")
        @Size(max = 180, message = "{validation.logradouro.size}")
        String logradouro,

        @Schema(description = "Número do endereço", example = SwaggerRequestExamples.NUMERO_ENDERECO)
        @NotBlank(message = "{validation.numero.notblank}")
        @Size(max = 20, message = "{validation.numero.size}")
        String numero,

        @Schema(description = "Complemento do endereço", example = SwaggerRequestExamples.COMPLEMENTO)
        @Size(max = 1000, message = "{validation.complemento.size}")
        String complemento,

        @Schema(description = "Indica se o endereço é principal", example = SwaggerRequestExamples.PRINCIPAL)
        @NotNull(message = "{validation.endereco.principal.notnull}")
        Boolean principal
) {
}