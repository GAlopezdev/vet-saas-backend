package com.veterinaria.dto.response;

import java.math.BigDecimal;

public record EmpresaResponse(
        Long idEmpresa,
        String nombreComercial,
        String descripcion,
        String horarioAtencion,
        String telefono,
        String direccion,
        String ciudad,
        String pais,
        String logoUrl,
        String bannerUrl,
        BigDecimal latitud,
        BigDecimal longitud,
        String tipoEmpresaNombre
) {}
