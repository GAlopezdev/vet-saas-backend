package com.veterinaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdopcionRequest(
        @NotNull Long mascotaId,
        @NotNull Long usuarioId,
        @NotBlank String titulo,
        @NotBlank String historia,
        String requisitos
) {}