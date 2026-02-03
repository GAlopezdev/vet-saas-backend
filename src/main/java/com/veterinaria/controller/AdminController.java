package com.veterinaria.controller;

import com.veterinaria.model.entity.Usuario;
import com.veterinaria.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/veterinarios/{id}/aprobar")
    public ResponseEntity<String> aprobarVet(@PathVariable Long id, @AuthenticationPrincipal Usuario admin) {
        adminService.aprobarVeterinario(id, admin);
        return ResponseEntity.ok("Veterinario aprobado y cuenta activada.");
    }

    @PatchMapping("/empresas/{id}/aprobar")
    public ResponseEntity<String> aprobarEmpresa(@PathVariable Long id, @AuthenticationPrincipal Usuario admin) {
        adminService.aprobarEmpresa(id, admin);
        return ResponseEntity.ok("Empresa aprobada y cuenta activada.");
    }
}
