package br.com.acta.mapper;

import br.com.acta.dto.onboarding.OnboardingResponseDTO;
import br.com.acta.entity.core.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OnboardingMapper {
    default OnboardingResponseDTO toResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new OnboardingResponseDTO(
                false,
                false,
                false,
                usuario.getEmpresa() == null ? null : usuario.getEmpresa().getId(),
                usuario.getEmpresa() == null ? null : usuario.getEmpresa().getStatus(),
                usuario.getId(),
                usuario.getTipo(),
                usuario.getStatus(),
                null);
    }

    default OnboardingResponseDTO toResponse(
            Usuario usuario,
            boolean empresaExistente,
            boolean conviteEnviado,
            boolean proximaEtapa) {
        OnboardingResponseDTO response = toResponse(usuario);
        if (response == null) return null;
        return new OnboardingResponseDTO(
                empresaExistente,
                conviteEnviado,
                proximaEtapa,
                response.idEmpresa(),
                response.statusEmpresa(),
                response.idUsuario(),
                response.tipoUsuario(),
                response.statusUsuario(),
                response.mensagem());
    }

    default OnboardingResponseDTO solicitarDadosEmpresa() {
        return new OnboardingResponseDTO(false, false, true, null, null, null, null, null, null);
    }
}