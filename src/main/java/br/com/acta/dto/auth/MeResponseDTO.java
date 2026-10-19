package br.com.acta.dto.auth;

import br.com.acta.entity.enums.EstadoPerfil;
import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.TipoUsuario;

public record MeResponseDTO(
        String firebaseUid,
        Long idUsuario,
        Long idEmpresa,
        Long idColaborador,
        String nome,
        String email,
        String nomeEmpresa,
        TipoUsuario tipo,
        boolean permissaoGestor,
        StatusGeral status,
        EstadoPerfil estadoPerfil
) { }