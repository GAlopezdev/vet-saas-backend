package com.veterinaria.dto.request;

import com.veterinaria.model.entity.Order;
import com.veterinaria.model.enums.EstadoOrden;
import jakarta.validation.constraints.NotNull;

public record UpdateEstadoOrdenRequest(
        @NotNull(message = "El nuevo estado no puede ser nulo")
        Order.OrderStatus nuevoEstado
) {}