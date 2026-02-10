package com.veterinaria.service;

import com.veterinaria.dto.request.ProductoRequestDTO;
import com.veterinaria.dto.response.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO, MultipartFile imagen);

    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO, MultipartFile imagen);

    void eliminarProducto(Long id);

    Page<ProductoResponseDTO> obtenerTodos(Pageable pageable);

    Page<ProductoResponseDTO> obtenerMisProductos(Pageable pageable);
}
