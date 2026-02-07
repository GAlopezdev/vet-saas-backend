package com.veterinaria.dto.request;

import jakarta.validation.constraints.*;

public record RegistroEmpresaRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo es inválido")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,64}$",
                message = "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales"
        )
        String contrasenia,

                @NotNull(message = "El tipo de empresa es obligatorio")
        Long idTipoEmpresa,

        @NotBlank(message = "El nombre comercial es obligatorio")
        String nombreComercial,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
        String descripcion,

        @NotBlank(message = "El RUC es obligatorio")
        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos numéricos")
        String ruc,

        @NotBlank(message = "La razón social es obligatoria")
        String razonSocial,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotBlank(message = "El país es obligatorio")
        String pais,

        @NotBlank(message = "La ciudad es obligatoria")
        String ciudad,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion
) {}