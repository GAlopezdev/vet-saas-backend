package com.veterinaria.controller;

import com.veterinaria.dto.request.AdopcionRequest;
import com.veterinaria.dto.request.DecisionAdopcionRequest;
import com.veterinaria.dto.request.SolicitudAdopcionRequest;
import com.veterinaria.dto.response.AdopcionResponse;
import com.veterinaria.dto.response.SolicitudDetalleResponse;
import com.veterinaria.service.impl.AdopcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final AdopcionService adopcionService;

    @PostMapping("/publicar")
    public ResponseEntity<AdopcionResponse> publicar(@Valid @RequestBody AdopcionRequest request) {
        return ResponseEntity.ok(adopcionService.publicar(request));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<AdopcionResponse>> listarDisponibles() {
        return ResponseEntity.ok(adopcionService.listarDisponibles());
    }

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarAdopcion(@Valid @RequestBody SolicitudAdopcionRequest request) {
        return ResponseEntity.ok(adopcionService.enviarSolicitud(request));
    }

    @GetMapping("/{publicacionId}/solicitudes")
    public ResponseEntity<List<SolicitudDetalleResponse>> listarSolicitudes(@PathVariable Long publicacionId) {
        return ResponseEntity.ok(adopcionService.listarInteresados(publicacionId));
    }

    @PatchMapping("/solicitudes/{solicitudId}/decision")
    public ResponseEntity<String> decidirAdopcion(
            @PathVariable Long solicitudId,
            @Valid @RequestBody DecisionAdopcionRequest request) {
        return ResponseEntity.ok(adopcionService.procesarDecision(solicitudId, request));
    }
}