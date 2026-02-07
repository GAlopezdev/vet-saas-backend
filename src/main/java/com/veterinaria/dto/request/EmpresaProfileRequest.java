package com.veterinaria.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EmpresaProfileRequest(
        @NotBlank(message = "El nombre comercial no puede estar vacío")
        @Size(max = 150)
        String nombreComercial,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 20)
        String telefono,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200)
        String direccion,

        @Size(max = 1000, message = "La descripción es muy larga")
        String descripcion,

        @NotEmpty(message = "Debe enviar al menos un horario")
        @Valid
        List<HorarioRequest> horarios
) {}
