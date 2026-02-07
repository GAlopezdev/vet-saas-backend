package com.veterinaria.dto.response;

public record VeterinarioBusquedaResponse(
        Long idVeterinario,
        String nombresCompletos,
        String especialidad,
        Integer aniosExperiencia,
        String fotoPerfil,
        String numeroColegiatura
) {}
