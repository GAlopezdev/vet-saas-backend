package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitudAdopcionRequest(
        @NotNull Long publicacionId,
        @NotNull Long interesadoUsuarioId,
        @NotBlank String mensaje
) {}