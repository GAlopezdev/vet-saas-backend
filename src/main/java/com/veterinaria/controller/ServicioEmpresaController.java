package com.veterinaria.controller;

import com.veterinaria.dto.request.ServicioRequest;
import com.veterinaria.dto.response.ServicioResponse;
import com.veterinaria.service.ServicioEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresa/servicios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPRESA')")
public class ServicioEmpresaController {

    private final ServicioEmpresaService servicioService;

    @PostMapping
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioRequest request) {
        ServicioResponse nuevoServicio = servicioService.crearServicio(request);
        return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listar() {
        List<ServicioResponse> servicios = servicioService.listarMisServicios();
        return ResponseEntity.ok(servicios);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ServicioRequest request) {
        ServicioResponse actualizado = servicioService.actualizarServicio(id, request);
        return ResponseEntity.ok(actualizado);
    }

}
