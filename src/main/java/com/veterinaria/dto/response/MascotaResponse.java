package com.veterinaria.dto.response;

import com.veterinaria.model.entity.Mascota;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaResponse(
        Long idMascota,
        String nombre,
        String especie,
        String raza,
        Mascota.SexoMascota sexo,
        LocalDate fechaNacimiento,
        BigDecimal peso,
        String color,
        String fotoUrl,
        String observaciones,
        Boolean estado
) {}