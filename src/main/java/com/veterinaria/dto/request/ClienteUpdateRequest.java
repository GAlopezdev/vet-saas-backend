package com.veterinaria.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequest(
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombres,

        @Size(min = 2, max = 100, message = "El apellido paterno debe tener entre 2 y 100 caracteres")
        String apepa,

        @Size(max = 100)
        String apema,

        @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe contener entre 7 y 15 dígitos numéricos")
        String telefono,

        @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
        String direccion,

        @Size(max = 100)
        String ciudad,

        @Size(max = 100)
        String pais,

        @Size(max = 255)
        String fotoUrl
) {}
