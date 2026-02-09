package com.veterinaria.service;

import com.veterinaria.dto.request.ProductoRequestDTO;
import com.veterinaria.dto.response.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO);

    Page<ProductoResponseDTO> obtenerTodos(Pageable pageable);

    Page<ProductoResponseDTO> obtenerMisProductos(Pageable pageable);
}
