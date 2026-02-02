package com.veterinaria.dto.response;

public record VeterinarioResponse(
        Long idVeterinario,
        String nombres,
        String apepa,
        String apema,
        String telefono,
        String especialidad,
        Integer aniosExperiencia,
        String fotoPerfil
) {}
