package br.com.acta.dto.auth;

import br.com.acta.entity.enums.TipoUsuario;

public record AuthContextResponseDTO(
        Long idUsuario,
        String nome,
        Long idEmpresa,
        Long idColaborador,
        TipoUsuario tipo,
        boolean permissaoGestor
) { }