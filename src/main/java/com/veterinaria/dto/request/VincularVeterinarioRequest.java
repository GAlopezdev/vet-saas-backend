package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VincularVeterinarioRequest(
        @NotNull(message = "El veterinario es obligatorio")
        Long veterinarioId,

        @NotBlank(message = "El cargo es obligatorio")
        String cargo
) {}
