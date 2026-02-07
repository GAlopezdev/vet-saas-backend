package com.veterinaria.controller;

import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.request.VincularVeterinarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.dto.response.VeterinarioAsociadoResponse;
import com.veterinaria.dto.response.VeterinarioBusquedaResponse;
import com.veterinaria.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // --- Gestión de Veterinarios ---

    @PostMapping("/veterinarios")
    public ResponseEntity<String> vincularVeterinario(@Valid @RequestBody VincularVeterinarioRequest request) {
        empresaService.asociarVeterinario(request);
        return new ResponseEntity<>("Veterinario vinculado exitosamente", HttpStatus.CREATED);
    }

    @GetMapping("/veterinarios")
    public ResponseEntity<List<VeterinarioAsociadoResponse>> listarMisVeterinarios() {
        return ResponseEntity.ok(empresaService.listarVeterinariosAsociados());
    }

    @GetMapping("/buscar-veterinarios")
    public ResponseEntity<List<VeterinarioBusquedaResponse>> buscarVeterinarios(
            @RequestParam String filtro) {
        return ResponseEntity.ok(empresaService.buscarVeterinariosParaAsociar(filtro));
    }

    @DeleteMapping("/veterinarios/{idVeterinario}")
    public ResponseEntity<Void> desvincularVeterinario(@PathVariable Long idVeterinario) {
        empresaService.desvincularVeterinario(idVeterinario);
        return ResponseEntity.noContent().build();
    }
}