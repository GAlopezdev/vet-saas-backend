package com.veterinaria.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record HorarioRequest(
        @NotNull(message = "El día de la semana es obligatorio")
        @Min(value = 1, message = "El día debe ser entre 1 (Lunes) y 7 (Domingo)")
        @Max(value = 7, message = "El día debe ser entre 1 (Lunes) y 7 (Domingo)")
        Integer diaSemana,

        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora de apertura inválido (HH:mm)")
        String horaApertura,

        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora de cierre inválido (HH:mm)")
        String horaCierre,

        @NotNull(message = "Debe especificar si está cerrado")
        Boolean estaCerrado
) {}