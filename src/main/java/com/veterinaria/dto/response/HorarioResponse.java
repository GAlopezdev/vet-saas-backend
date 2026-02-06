package com.veterinaria.dto.response;

import java.time.LocalTime;

public record HorarioResponse(
        Long idHorario,
        Integer diaSemana,
        LocalTime horaApertura,
        LocalTime horaCierre,
        boolean estaCerrado
) {}
