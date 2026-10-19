package br.com.acta.mapper;

import br.com.acta.dto.onboarding.OnboardingResponseDTO;
import br.com.acta.entity.core.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OnboardingMapper {
    @Mapping(target = "empresaExistente", constant = "false")
    @Mapping(target = "conviteEnviado", constant = "false")
    @Mapping(target = "proximaEtapa", constant = "false")
    @Mapping(source = "empresa.id", target = "idEmpresa")
    @Mapping(source = "empresa.status", target = "statusEmpresa")
    @Mapping(source = "id", target = "idUsuario")
    @Mapping(source = "tipo", target = "tipoUsuario")
    @Mapping(source = "status", target = "statusUsuario")
    @Mapping(target = "mensagem", ignore = true)
    OnboardingResponseDTO toResponse(Usuario usuario);

    default OnboardingResponseDTO toResponse(
            Usuario usuario,
            boolean empresaExistente,
            boolean conviteEnviado,
            boolean proximaEtapa) {
        OnboardingResponseDTO response = toResponse(usuario);
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
