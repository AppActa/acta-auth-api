package br.com.acta.common.config.swagger.openapi;

import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.config.swagger.annotation.ApiAuthenticationConflictResponses;
import br.com.acta.common.config.swagger.annotation.ApiAuthenticationResponses;
import br.com.acta.common.config.swagger.annotation.ApiBusinessRuleResponse;
import br.com.acta.common.config.swagger.annotation.ApiNotFoundResponse;
import br.com.acta.common.config.swagger.examples.SwaggerOpenapiDescriptions;
import br.com.acta.dto.auth.AtivarRequestDTO;
import br.com.acta.dto.auth.AuthContextResponseDTO;
import br.com.acta.dto.auth.ConviteRequestDTO;
import br.com.acta.dto.auth.MeResponseDTO;
import br.com.acta.dto.core.colaborador.ColaboradorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = SwaggerOpenapiDescriptions.AUTH_CONTROLLER)
public interface AuthOpenapi {

    @Operation(summary = "Consulta o contexto de acesso ativo")
    @ApiResponse(responseCode = "200", description = "Dados do usuário, empresa e colaborador ativos, incluindo o nome do usuário", content = @Content(schema = @Schema(implementation = AuthContextResponseDTO.class)))
    @ApiResponse(responseCode = "503", description = "Banco de dados indisponível")
    @ApiAuthenticationResponses
    ResponseEntity<AuthContextResponseDTO> contexto();

    @Operation(summary = "Consulta o usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Identidade Firebase válida; estadoPerfil informa PERFIL_CRIADO ou CADASTRO_NAO_INICIADO", content = @Content(schema = @Schema(implementation = MeResponseDTO.class)))
    @ApiAuthenticationResponses
    ResponseEntity<MeResponseDTO> me(@Parameter(hidden = true) FirebaseIdentity identity);

    @Operation(summary = "Ativa o usuário autenticado com código de convite")
    @ApiResponse(responseCode = "200", description = "Usuário ativado", content = @Content(schema = @Schema(implementation = MeResponseDTO.class)))
    @ApiAuthenticationConflictResponses
    @ApiNotFoundResponse
    ResponseEntity<MeResponseDTO> ativar(@Parameter(hidden = true) FirebaseIdentity identity, @RequestBody(description = "Código alfanumérico recebido por e-mail", required = true) @Valid AtivarRequestDTO request);

    @Operation(summary = "Cadastra um colaborador e envia convite de acesso")
    @ApiResponse(responseCode = "201", description = "Convite criado e enviado", content = @Content(schema = @Schema(implementation = ColaboradorResponseDTO.class)))
    @ApiAuthenticationConflictResponses
    @ApiBusinessRuleResponse
    ResponseEntity<ColaboradorResponseDTO> convidar(@RequestBody(description = "Dados do colaborador e acesso", required = true) @Valid ConviteRequestDTO request);

    @Operation(summary = "Reenvia o convite de um colaborador pendente")
    @ApiResponse(responseCode = "204", description = "Convite reenviado")
    @ApiAuthenticationConflictResponses
    @ApiBusinessRuleResponse
    ResponseEntity<Void> reenviarConvite(@Parameter(description = "ID do colaborador") Long idColaborador);
}