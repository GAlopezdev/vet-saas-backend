package com.veterinaria.dto.mercadopago;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotNull(message = "El usuario ID es requerido")
        Long usuarioId,

        @NotEmpty(message = "Debe incluir al menos un item")
        @Valid
        List<CartItemRequest> items
) {}