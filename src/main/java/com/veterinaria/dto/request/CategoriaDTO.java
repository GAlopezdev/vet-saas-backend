package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        Long idCategoria,
        @NotBlank(message = "El nombre de categoría es obligatorio")
        String nombre,
        String descripcion
) {}
