package br.com.acta.dto.core.colaborador;

import br.com.acta.entity.enums.StatusGeral;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record ColaboradorResponseDTO(
        Long id,
        String cpf,
        String nome,
        String nickname,
        String cargo,
        String area,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataContratacao,

        Boolean permissaoGestor,
        StatusGeral status,
        Long idUsuario,
        Long idEmpresa,
        List<TelefoneResponseDTO> telefones,
        List<EmailResponseDTO> emails,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime criadoEm,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime atualizadoEm
) {
    public record TelefoneResponseDTO(
            Long id,
            String numero,
            Boolean principal,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
            OffsetDateTime criadoEm
    ) {
    }

    public record EmailResponseDTO(
            Long id,
            String email,
            Boolean principal,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
            OffsetDateTime criadoEm
    ) {
    }
}