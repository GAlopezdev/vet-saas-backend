package com.veterinaria.dto.response;

import com.veterinaria.model.entity.SolicitudAdopcion;

import java.time.LocalDateTime;

public record SolicitudDetalleResponse(
        Long idSolicitud,
        Long interesadoId,
        String mensaje,
        SolicitudAdopcion.EstadoSolicitud estado,
        LocalDateTime fechaSolicitud
) {}