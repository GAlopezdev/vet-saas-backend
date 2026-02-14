package com.veterinaria.controller;

import com.veterinaria.dto.request.MascotaRequest;
import com.veterinaria.dto.response.MascotaResponse;
import com.veterinaria.model.entity.Mascota;
import com.veterinaria.service.impl.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Mascota> crear(
            @RequestPart("data") @Valid MascotaRequest request,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        return ResponseEntity.ok(mascotaService.registrarMascota(request, imagen));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MascotaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mascotaService.listarMascotasPorUsuario(usuarioId));
    }
}