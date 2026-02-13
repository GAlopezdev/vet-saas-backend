package com.veterinaria.dto.mercadopago;

import com.veterinaria.model.enums.ItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CartItemRequest(

        @NotNull(message = "El tipo de item es requerido")
        ItemType tipoItem,

        @NotNull(message = "El ID del item es requerido")
        Long itemId,

        @NotNull(message = "La cantidad es requerida")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad,

        @NotNull(message = "El precio unitario es requerido")
        @Min(value = 0, message = "El precio debe ser mayor a 0")
        BigDecimal precioUnitario,

        String titulo,

        String descripcion
) {}