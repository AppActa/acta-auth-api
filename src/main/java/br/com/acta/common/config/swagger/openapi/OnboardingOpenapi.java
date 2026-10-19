package br.com.acta.common.config.swagger.openapi;

import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.config.swagger.annotation.ApiAuthenticationConflictResponses;
import br.com.acta.common.config.swagger.annotation.ApiBadRequestResponse;
import br.com.acta.common.config.swagger.examples.SwaggerOpenapiDescriptions;
import br.com.acta.dto.onboarding.OnboardingInicioRequestDTO;
import br.com.acta.dto.onboarding.OnboardingRequestDTO;
import br.com.acta.dto.onboarding.OnboardingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Onboarding", description = SwaggerOpenapiDescriptions.ONBOARDING_CONTROLLER)
public interface OnboardingOpenapi {
    @Operation(summary = "Verifica a empresa e inicia o onboarding do gestor")
    @ApiResponse(responseCode = "200", description = "Empresa identificada, orientação para solicitar convite a um ADMIN ativo ou próxima etapa liberada", content = @Content(schema = @Schema(implementation = OnboardingResponseDTO.class)))
    @ApiAuthenticationConflictResponses
    @ApiBadRequestResponse
    ResponseEntity<OnboardingResponseDTO> iniciar(@Parameter(hidden = true) FirebaseIdentity identity, @RequestBody(description = "Dados do gestor e CNPJ da empresa", required = true, content = @Content(schema = @Schema(implementation = OnboardingInicioRequestDTO.class))) OnboardingInicioRequestDTO dto);

    @Operation(summary = "Cadastra a empresa e seu primeiro gestor")
    @ApiResponse(responseCode = "201", description = "Empresa criada e primeiro gestor ativado", content = @Content(schema = @Schema(implementation = OnboardingResponseDTO.class)))
    @ApiAuthenticationConflictResponses
    @ApiBadRequestResponse
    ResponseEntity<OnboardingResponseDTO> cadastrarEmpresa(@Parameter(hidden = true) FirebaseIdentity identity, @RequestBody(description = "Dados do gestor, empresa e endereço principal", required = true, content = @Content(schema = @Schema(implementation = OnboardingRequestDTO.class))) OnboardingRequestDTO dto);

}