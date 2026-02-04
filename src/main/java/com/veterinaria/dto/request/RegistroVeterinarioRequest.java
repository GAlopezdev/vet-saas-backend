package com.veterinaria.dto.request;

import jakarta.validation.constraints.*;

public record RegistroVeterinarioRequest(
        @NotBlank
        @Email
        String correo,
        @NotBlank
        @Size(min = 8)
        String contrasenia,
        @NotBlank
        String nombres,
        @NotBlank
        String apepa,
        @NotBlank
        String apema,
        @NotBlank
        String telefono,
        @NotBlank
        String especialidad,
        @NotNull
        @PositiveOrZero
        Integer aniosExperiencia,
        @NotBlank
        String numeroColegiatura
) {}
