package com.veterinaria.dto.mercadopago;

import java.math.BigDecimal;

public record PaymentResponse(
        Long ordenId,
        String preferenceId,
        String initPoint,
        BigDecimal total,
        String estado
) {}