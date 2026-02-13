package com.veterinaria.dto.mercadopago;

public record WebhookNotification(
        String action,
        String apiVersion,
        WebhookData data,
        String dateCreated,
        Long id,
        Boolean liveMode,
        String type,
        String userId
) {
    public record WebhookData(
            String id
    ) {}
}