package com.veterinaria.controller;

import com.veterinaria.dto.external.RucExternalResponse;
import com.veterinaria.service.impl.ConsultaRucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultasController {

    private final ConsultaRucService consultaRucService;

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<RucExternalResponse> testRuc(@PathVariable String ruc) {
        if (ruc.length() != 11) {
            return ResponseEntity.badRequest().build();
        }
        RucExternalResponse data = consultaRucService.buscarRuc(ruc);
        return ResponseEntity.ok(data);
    }

}
