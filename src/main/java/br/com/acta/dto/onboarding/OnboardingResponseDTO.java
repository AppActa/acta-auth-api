package br.com.acta.dto.onboarding;

import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.TipoUsuario;

public record OnboardingResponseDTO(
        Boolean empresaExistente,
        Boolean conviteEnviado,
        Boolean proximaEtapa,
        Long idEmpresa,
        StatusGeral statusEmpresa,
        Long idUsuario,
        TipoUsuario tipoUsuario,
        StatusGeral statusUsuario,
        String mensagem
) {
}