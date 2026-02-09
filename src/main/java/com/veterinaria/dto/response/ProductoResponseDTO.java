package com.veterinaria.dto.response;

public record ProductoResponseDTO(
        Long idProducto,
        String nombre,
        String descripcion,
        java.math.BigDecimal precio,
        Integer stock,
        String imagenUrl,
        String estado,
        String nombreVendedor,
        String nombreSubcategoria
) {
}
