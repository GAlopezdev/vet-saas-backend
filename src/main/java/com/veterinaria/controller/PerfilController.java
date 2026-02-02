package com.veterinaria.controller;

import com.veterinaria.dto.request.ClienteUpdateRequest;
import com.veterinaria.dto.request.EmpresaUpdateRequest;
import com.veterinaria.dto.request.VeterinarioUpdateRequest;
import com.veterinaria.dto.response.ClienteUpdateResponse;
import com.veterinaria.dto.response.EmpresaResponse;
import com.veterinaria.dto.response.VeterinarioResponse;
import com.veterinaria.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @PatchMapping(value = "/cliente", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteUpdateResponse> patchCliente(
            @Valid @RequestPart("dto") ClienteUpdateRequest dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(perfilService.patchCliente(dto, imagen));
    }

    @PatchMapping(value = "/veterinario", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<VeterinarioResponse> patchVeterinario(
            @Valid @RequestPart("dto") VeterinarioUpdateRequest dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(perfilService.patchVeterinario(dto, imagen));
    }

    @PatchMapping(value = "/empresa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<EmpresaResponse> patchEmpresa(
            @Valid @RequestPart("dto") EmpresaUpdateRequest dto,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "banner", required = false) MultipartFile banner) {
        return ResponseEntity.ok(perfilService.patchEmpresa(dto, logo, banner));
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(perfilService.obtenerDatosCompletos(correo));
    }
}
