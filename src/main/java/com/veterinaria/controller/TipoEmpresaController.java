package com.veterinaria.controller;

import com.veterinaria.dto.response.TipoEmpresaResponse;
import com.veterinaria.service.TipoEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-empresa")
@RequiredArgsConstructor
public class TipoEmpresaController {

    private final TipoEmpresaService tipoEmpresaService;

    @GetMapping
    public ResponseEntity<List<TipoEmpresaResponse>> listarTipos() {
        return ResponseEntity.ok(tipoEmpresaService.listarTipos());
    }

}