package com.veterinaria.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicioResponse(
        Long idServicio,
        String nombreServicio,
        String descripcion,
        BigDecimal precio,
        Boolean activo,
        LocalDateTime createdAt
) {}
