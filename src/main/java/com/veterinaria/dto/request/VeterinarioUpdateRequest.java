package com.veterinaria.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record VeterinarioUpdateRequest(
        @Size(min = 2, max = 100)
        String nombres,

        @Size(min = 2, max = 100)
        String apepa,

        @Size(max = 100)
        String apema,

        @Size(max = 20)
        String telefono,

        @Size(max = 100)
        String especialidad,

        @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
        @Max(value = 10, message = "Los años de experiencia no pueden exceder 10")
        Integer aniosExperiencia,

        @Size(max = 255)
        String fotoUrl
) {}
