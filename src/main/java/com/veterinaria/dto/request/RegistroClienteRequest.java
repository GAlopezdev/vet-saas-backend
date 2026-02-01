package com.veterinaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroClienteRequest(
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
        String pais
) {}
