package com.veterinaria.dto.external;

public record RucExternalResponse(
        String ruc,
        String razonSocial,
        String direccion,
        String estado,
        String condicion,
        String departamento,
        String provincia,
        String distrito
) {}
