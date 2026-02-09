package com.veterinaria.controller;

import com.veterinaria.dto.request.ProductoRequestDTO;
import com.veterinaria.dto.response.ProductoResponseDTO;
import com.veterinaria.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductos(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(productoService.obtenerTodos(pageable));
    }

    @GetMapping("/mis-productos")
    public ResponseEntity<Page<ProductoResponseDTO>> misProductos(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productoService.obtenerMisProductos(pageable));
    }
}
