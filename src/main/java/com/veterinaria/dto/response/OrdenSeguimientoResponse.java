package com.veterinaria.dto.response;

import com.veterinaria.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenSeguimientoResponse(
        Long idOrden,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaPago,
        BigDecimal total,
        Order.OrderStatus estadoActual,
        String paymentIdMp
) {}