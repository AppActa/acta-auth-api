package br.com.acta.dto.onboarding;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import br.com.acta.entity.enums.TamanhoEmpresa;
import br.com.acta.entity.enums.UF;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;

public record OnboardingRequestDTO(
        @Schema(description = "Dados do primeiro gestor da empresa")
        @NotNull(message = "{validation.onboarding.gestor.notnull}")
        @Valid
        Gestor gestor,

        @Schema(description = "Dados da empresa")
        @NotNull(message = "{validation.onboarding.empresa.notnull}")
        @Valid
        Empresa empresa,

        @Schema(description = "Endereço principal da empresa")
        @NotNull(message = "{validation.onboarding.endereco.notnull}")
        @Valid
        Endereco endereco
) {
    public record Gestor(
            @Schema(description = "CPF do gestor", example = SwaggerRequestExamples.CPF)
            @CPF(message = "{validation.colaborador.cpf.invalid}")
            @NotBlank(message = "{validation.colaborador.cpf.notblank}")
            @Size(min = 11, max = 11, message = "{validation.colaborador.cpf.size}")
            String cpf,

            @Schema(description = "Nome do gestor", example = SwaggerRequestExamples.NOME)
            @NotBlank(message = "{validation.colaborador.nome.notblank}")
            @Size(max = 160, message = "{validation.nome.size}")
            String nome,

            @Schema(description = "Cargo do gestor", example = SwaggerRequestExamples.CARGO)
            @NotBlank(message = "{validation.colaborador.cargo.notblank}")
            @Size(max = 100, message = "{validation.colaborador.cargo.size}")
            String cargo,

            @Schema(description = "Área do gestor", example = SwaggerRequestExamples.AREA)
            @NotBlank(message = "{validation.area.notblank}")
            @Size(max = 100, message = "{validation.area.size}")
            String area,

            @Schema(description = "Data de nascimento do gestor", example = SwaggerRequestExamples.DATA_NASCIMENTO, format = "yyyy-MM-dd", type = "string")
            @JsonFormat(pattern = "yyyy-MM-dd")
            @NotNull(message = "{validation.colaborador.dataNascimento.notnull}")
            @Past(message = "{validation.colaborador.dataNascimento.past}")
            LocalDate dataNascimento,

            @Schema(description = "Data de contratação do gestor", example = SwaggerRequestExamples.DATA_CONTRATACAO, format = "yyyy-MM-dd", type = "string")
            @JsonFormat(pattern = "yyyy-MM-dd")
            @NotNull(message = "{validation.colaborador.dataContratacao.notnull}")
            @PastOrPresent(message = "{validation.colaborador.dataContratacao.pastorpresent}")
            LocalDate dataContratacao,

            @Schema(description = "E-mail de acesso do gestor", example = SwaggerRequestExamples.EMAIL)
            @NotBlank(message = "{validation.email.notblank}")
            @Email(message = "{validation.email.invalid}")
            @Size(max = 254, message = "{validation.email.size}")
            String email
    ) {}

    public record Empresa(
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
            TamanhoEmpresa tamanhoEmpresa,

            @Schema(description = "Setor da empresa", example = SwaggerRequestExamples.SETOR)
            @NotBlank(message = "{validation.empresa.setor.notblank}")
            @Size(max = 100, message = "{validation.empresa.setor.size}")
            String setorEmpresa,

            @Schema(description = "E-mail principal da empresa", example = SwaggerRequestExamples.EMAIL)
            @NotBlank(message = "{validation.email.notblank}")
            @Email(message = "{validation.email.invalid}")
            @Size(max = 254, message = "{validation.email.size}")
            String emailEmpresa,

            @Schema(description = "Telefone principal da empresa", example = SwaggerRequestExamples.TELEFONE)
            @NotBlank(message = "{validation.telefone.numero.notblank}")
            @Pattern(regexp = "^\\d{10,11}$", message = "{validation.telefone.numero.pattern}")
            String telefoneEmpresa
    ) {}

    public record Endereco(
            @Schema(description = "CEP do endereço", example = SwaggerRequestExamples.CEP)
            @NotBlank(message = "{validation.cep.notblank}")
            @Pattern(regexp = "\\d{8}", message = "{validation.cep.pattern}")
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
            String numeroEndereco,

            @Schema(description = "Complemento do endereço", example = SwaggerRequestExamples.COMPLEMENTO)
            @Size(max = 1000, message = "{validation.complemento.size}")
            String complemento
    ) {}
}