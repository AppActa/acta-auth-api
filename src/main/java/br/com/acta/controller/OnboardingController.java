package br.com.acta.controller;

import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.config.swagger.openapi.OnboardingOpenapi;
import br.com.acta.dto.onboarding.OnboardingInicioRequestDTO;
import br.com.acta.dto.onboarding.OnboardingRequestDTO;
import br.com.acta.dto.onboarding.OnboardingResponseDTO;
import br.com.acta.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/onboarding", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OnboardingController implements OnboardingOpenapi {
    private final OnboardingService service;

    @PostMapping("/inicio")
    @Override
    public ResponseEntity<OnboardingResponseDTO> iniciar(@AuthenticationPrincipal FirebaseIdentity identity, @RequestBody @Valid OnboardingInicioRequestDTO dto) {
        return ResponseEntity.ok(service.iniciar(identity, dto));
    }

    @PostMapping("/gestor")
    @Override
    public ResponseEntity<OnboardingResponseDTO> cadastrarEmpresa(@AuthenticationPrincipal FirebaseIdentity identity, @RequestBody @Valid OnboardingRequestDTO dto) {
        return ResponseEntity.status(201).body(service.cadastrarEmpresa(identity, dto));
    }
}
