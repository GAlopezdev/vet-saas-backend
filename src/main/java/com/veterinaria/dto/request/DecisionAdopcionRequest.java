package com.veterinaria.dto.request;

import com.veterinaria.model.entity.SolicitudAdopcion;
import jakarta.validation.constraints.NotNull;

public record DecisionAdopcionRequest(
        @NotNull SolicitudAdopcion.EstadoSolicitud nuevoEstado
) {}