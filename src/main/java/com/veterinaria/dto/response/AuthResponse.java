package com.veterinaria.dto.response;

public record AuthResponse(
        String token,
        String correo,
        String rol
) {}
