package com.veterinaria.dto.request;

import com.veterinaria.model.entity.Mascota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaRequest(
        @NotNull(message = "El ID de usuario es obligatorio")
        Long usuarioId,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "La especie es obligatoria")
        String especie,

        String raza,

        @NotNull(message = "El sexo es obligatorio (MACHO/HEMBRA)")
        Mascota.SexoMascota sexo,

        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
        LocalDate fechaNacimiento,

        BigDecimal peso,
        String color,
        String fotoUrl,
        String observaciones
) {}