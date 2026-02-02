package com.veterinaria.dto.response;

public record ClienteUpdateResponse (
        Long idPerfil,
        String nombres,
        String apepa,
        String apema,
        String telefono,
        String direccion,
        String ciudad,
        String pais,
        String fotoPerfil
){}
