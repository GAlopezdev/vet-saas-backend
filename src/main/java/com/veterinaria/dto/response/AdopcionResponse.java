package com.veterinaria.dto.response;

import java.time.LocalDateTime;

public record AdopcionResponse(
        Long idAdopcion,
        String titulo,
        String nombreMascota,
        String especie,
        String fotoUrl,
        String historia,
        LocalDateTime fechaPublicacion
) {}