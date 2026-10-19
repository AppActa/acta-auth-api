package br.com.acta.dto.auth;

import br.com.acta.common.config.swagger.examples.SwaggerRequestExamples;
import br.com.acta.entity.enums.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ConviteRequestDTO(
        @Schema(description = "CPF do colaborador", example = SwaggerRequestExamples.CPF)
        @CPF(message = "{validation.colaborador.cpf.invalid}")
        @NotBlank(message = "{validation.colaborador.cpf.notblank}")
        @Size(min = 11, max = 11, message = "{validation.colaborador.cpf.size}")
        String cpf,

        @Schema(description = "Nome do colaborador e usuário", example = SwaggerRequestExamples.NOME)
        @NotBlank(message = "{validation.colaborador.nome.notblank}")
        @Size(max = 160, message = "{validation.nome.size}")
        String nome,

        @Schema(description = "Cargo do colaborador", example = SwaggerRequestExamples.CARGO)
        @NotBlank(message = "{validation.colaborador.cargo.notblank}")
        @Size(max = 100, message = "{validation.colaborador.cargo.size}")
        String cargo,

        @Schema(description = "Área do colaborador", example = SwaggerRequestExamples.AREA)
        @NotBlank(message = "{validation.area.notblank}")
        @Size(max = 100, message = "{validation.area.size}")
        String area,

        @NotNull(message = "{validation.colaborador.dataNascimento.notnull}")
        @Past(message = "{validation.colaborador.dataNascimento.past}")
        LocalDate dataNascimento,

        @NotNull(message = "{validation.colaborador.dataContratacao.notnull}")
        @PastOrPresent(message = "{validation.colaborador.dataContratacao.pastorpresent}")
        LocalDate dataContratacao,

        @NotNull(message = "{validation.colaborador.permissaoGestor.notnull}")
        Boolean permissaoGestor,

        @Schema(description = "E-mail principal do colaborador e e-mail de acesso", example = SwaggerRequestExamples.EMAIL)
        @NotBlank(message = "{validation.email.notblank}")
        @Email(message = "{validation.email.invalid}")
        @Size(max = 254, message = "{validation.email.size}")
        String email,

        @Schema(description = "Telefone principal do colaborador", example = SwaggerRequestExamples.TELEFONE)
        @NotBlank(message = "{validation.telefone.numero.notblank}")
        @Size(min = 9, max = 11, message = "{validation.telefone.numero.size}")
        @Pattern(regexp = "^\\d{9,11}$", message = "{validation.telefone.numero.pattern}")
        String telefone,

        @NotNull(message = "{validation.usuario.tipo.notnull}")
        TipoUsuario tipo
) {}