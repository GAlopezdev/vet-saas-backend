package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubcategoriaDTO(
        Long idSubcategoria,
        @NotBlank(message = "El nombre de subcategoría es obligatorio")
        String nombre,
        @NotNull(message = "El id de categoría es necesario")
        Long categoriaId,
        String nombreCategoria
) {
}
