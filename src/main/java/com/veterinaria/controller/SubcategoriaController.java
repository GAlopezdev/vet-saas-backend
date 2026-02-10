package com.veterinaria.controller;

import com.veterinaria.dto.request.SubcategoriaDTO;
import com.veterinaria.service.SubCategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subcategorias")
@RequiredArgsConstructor
public class SubcategoriaController {

    private final SubCategoriaService subcategoriaService;

    @GetMapping
    public ResponseEntity<List<SubcategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(subcategoriaService.listarTodas());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<SubcategoriaDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(subcategoriaService.listarPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<SubcategoriaDTO> crear(@Valid @RequestBody SubcategoriaDTO dto) {
        return new ResponseEntity<>(subcategoriaService.crearSubcategoria(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        subcategoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}