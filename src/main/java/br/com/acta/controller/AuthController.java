package br.com.acta.controller;

import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.config.swagger.openapi.AuthOpenapi;
import br.com.acta.dto.auth.AtivarRequestDTO;
import br.com.acta.dto.auth.ConviteRequestDTO;
import br.com.acta.dto.auth.MeResponseDTO;
import br.com.acta.dto.core.colaborador.ColaboradorResponseDTO;
import br.com.acta.service.AuthService;
import br.com.acta.service.ConviteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController implements AuthOpenapi {
    private final ConviteService conviteService;
    private final AuthService authService;

    @GetMapping("/me")
    @Override
    public ResponseEntity<MeResponseDTO> me(@AuthenticationPrincipal FirebaseIdentity identity) {
        return ResponseEntity.ok(authService.buscarParaMe(identity));
    }

    @PostMapping("/auth/ativar")
    @Override
    public ResponseEntity<MeResponseDTO> ativar(@AuthenticationPrincipal FirebaseIdentity identity, @RequestBody @Valid AtivarRequestDTO request) {
        return ResponseEntity.ok(conviteService.ativar(identity, request.token()));
    }

    @PostMapping("/empresas/me/convites")
    @Override
    public ResponseEntity<ColaboradorResponseDTO> convidar(@RequestBody @Valid ConviteRequestDTO request) {
        return ResponseEntity.status(201).body(conviteService.convidar(authService.atual().idEmpresa(), request));
    }

    @PostMapping("/colaborador/{idColaborador}/convite")
    @Override
    public ResponseEntity<Void> reenviarConvite(@PathVariable @Positive Long idColaborador) {
        conviteService.reenviar(idColaborador);
        return ResponseEntity.noContent().build();
    }
}