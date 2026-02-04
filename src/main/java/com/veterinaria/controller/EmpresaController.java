package com.veterinaria.controller;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.model.entity.Usuario;
import com.veterinaria.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@AuthenticationPrincipal Usuario usuario,@Valid @RequestBody EmpresaProfileRequest request) {
        empresaService.actualizarPerfilCompleto(usuario.getIdUsuario(), request);
        return ResponseEntity.ok("Perfil y horarios actualizados correctamente");
    }
}