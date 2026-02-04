package com.veterinaria.controller;

import com.veterinaria.dto.response.EmpresaSummaryResponse;
import com.veterinaria.dto.response.VeterinarioSummaryResponse;
import com.veterinaria.model.entity.Usuario;
import com.veterinaria.model.enums.EstadoRegistro;
import com.veterinaria.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/empresas")
    public ResponseEntity<Page<EmpresaSummaryResponse>> listarEmpresas(
            @RequestParam(required = false) EstadoRegistro estado,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.listarEmpresas(estado, pageable));
    }

    @GetMapping("/veterinarios")
    public ResponseEntity<Page<VeterinarioSummaryResponse>> listarVeterinarios(
            @RequestParam(required = false) EstadoRegistro estado,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.listarVeterinarios(estado, pageable));
    }
}
