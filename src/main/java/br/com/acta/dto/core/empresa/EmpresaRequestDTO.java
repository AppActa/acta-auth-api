package br.com.acta.dto.core.empresa;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import br.com.acta.dto.core.contato.email.EmailRequestDTO;
import br.com.acta.dto.core.contato.telefone.TelefoneRequestDTO;
import br.com.acta.dto.core.empresa.endereco.EnderecoRequestDTO;
import br.com.acta.entity.enums.TamanhoEmpresa;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;

public record EmpresaRequestDTO(
        @Schema(description = "CNPJ da empresa", example = SwaggerRequestExamples.CNPJ)
        @NotBlank(message = "{validation.empresa.cnpj.notblank}")
        @Size(min = 14, max = 14, message = "{validation.empresa.cnpj.size}")
        @CNPJ(message = "{validation.empresa.cnpj.invalid}")
        String cnpj,

        @Schema(description = "Nome da empresa", example = SwaggerRequestExamples.EMPRESA_NOME)
        @NotBlank(message = "{validation.empresa.nome.notblank}")
        @Size(max = 160, message = "{validation.empresa.nome.size}")
        String nome,

        @Schema(description = "Tamanho da empresa", example = SwaggerRequestExamples.TAMANHO_EMPRESA, implementation = TamanhoEmpresa.class)
        @NotNull(message = "{validation.empresa.tamanho.notnull}")
        TamanhoEmpresa tamanho,

        @Schema(description = "Setor da empresa", example = SwaggerRequestExamples.SETOR)
        @NotBlank(message = "{validation.empresa.setor.notblank}")
        @Size(max = 100, message = "{validation.empresa.setor.size}")
        String setor,

        @ArraySchema(schema = @Schema(implementation = TelefoneRequestDTO.class), minItems = 1, uniqueItems = true, arraySchema = @Schema(description = "Lista de telefones da empresa"))
        @NotEmpty(message = "{validation.telefones.notempty}")
        List<@Valid TelefoneRequestDTO> telefones,

        @ArraySchema(schema = @Schema(implementation = EmailRequestDTO.class), minItems = 1, uniqueItems = true, arraySchema = @Schema(description = "Lista de emails da empresa"))
        @NotEmpty(message = "{validation.emails.notempty}")
        List<@Valid EmailRequestDTO> emails,

        @ArraySchema(schema = @Schema(implementation = EnderecoRequestDTO.class), minItems = 1, uniqueItems = true, arraySchema = @Schema(description = "Lista de endereços da empresa"))
        @Schema(description = "Lista de endereços da empresa")
        @NotEmpty(message = "{validation.enderecos.notempty}")
        List<@Valid EnderecoRequestDTO> enderecos
) {}