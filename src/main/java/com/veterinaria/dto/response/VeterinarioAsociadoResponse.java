package com.veterinaria.dto.response;

public record VeterinarioAsociadoResponse(
        Long idVeterinario,
        String nombresCompletos,
        String especialidad,
        String cargo,
        String estadoVinculo
) {}
