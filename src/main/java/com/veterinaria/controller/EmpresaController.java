package com.veterinaria.controller;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.model.entity.Usuario;
import com.veterinaria.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping("/mis-horarios")
    public ResponseEntity<List<HorarioResponse>> obtenerMisHorarios() {
        return ResponseEntity.ok(empresaService.listarMisHorarios());
    }

    @PutMapping("/mis-horarios")
    public ResponseEntity<List<HorarioResponse>> actualizarHorarios(
            @Valid @RequestBody List<HorarioRequest> horarios) {
        return ResponseEntity.ok(empresaService.actualizarHorarios(horarios));
    }
}