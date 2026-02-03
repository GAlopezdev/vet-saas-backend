package com.veterinaria.dto.response;

import com.veterinaria.model.enums.EstadoRegistro;

import java.time.LocalDateTime;

public record EmpresaSummaryResponse(
        Long idEmpresa,
        String nombreComercial,
        String ruc,
        String razonSocial,
        String correo,
        String telefono,
        String pais,
        EstadoRegistro estadoRegistro,
        LocalDateTime createdAt
) {}
