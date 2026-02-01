package com.veterinaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroEmpresaRequest(
        @NotBlank
        @Email
        String correo,
        @NotBlank
        @Size(min = 8)
        String contrasenia,
        @NotNull
        Long idTipoEmpresa,
        @NotBlank
        String nombreComercial,
        @NotBlank
        String telefono,
        @NotBlank
        String pais,
        @NotBlank
        String ciudad,
        @NotBlank
        String direccion
) {}
