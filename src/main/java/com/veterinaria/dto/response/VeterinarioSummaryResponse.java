package com.veterinaria.dto.response;

import com.veterinaria.model.enums.EstadoRegistro;

import java.time.LocalDateTime;

public record VeterinarioSummaryResponse(
        Long idVeterinario,
        String nombres,
        String apepa,
        String apema,
        String correo,
        String especialidad,
        String numeroColegiatura,
        EstadoRegistro estadoRegistro
) {}
