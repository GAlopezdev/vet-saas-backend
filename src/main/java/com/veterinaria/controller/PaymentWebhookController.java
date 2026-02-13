package com.veterinaria.controller;

import com.veterinaria.dto.mercadopago.WebhookNotification;
import com.veterinaria.service.impl.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final PaymentWebhookService webhookService;

    /**
     * Endpoint que Mercado Pago llamará cuando haya actualizaciones de pago
     * URL configurada en mp.webhook-url
     */
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody(required = false) WebhookNotification notification,
            @RequestParam(value = "data.id", required = false) String dataId, // Forzamos el nombre con el punto
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "topic", required = false) String topic) {

        log.info("🔔 Webhook entrante - Params: data.id={}, id={}, type={}, topic={}", dataId, id, type, topic);

        // Si el body es nulo (petición por URL), construimos el objeto manualmente
        if (notification == null || (notification.type() == null && (type != null || topic != null))) {
            String finalId = (dataId != null) ? dataId : id;
            String finalType = (type != null) ? type : topic;

            notification = new WebhookNotification(
                    "create",
                    null,
                    new WebhookNotification.WebhookData(finalId),
                    null,
                    null,
                    null,
                    finalType,
                    null
            );
        }

        webhookService.processWebhook(notification);
        return ResponseEntity.ok().build();
    }
    /**
     * URLs de redirección después del pago
     */
    @GetMapping("/exito")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String external_reference,
            @RequestParam(required = false) String preference_id) {

        log.info("✅ Pago exitoso - Payment ID: {}, External Ref: {}", payment_id, external_reference);

        // Redirigir al frontend con los parámetros
        return ResponseEntity.ok("Pago procesado exitosamente. Puedes cerrar esta ventana.");
    }

    @GetMapping("/fallo")
    public ResponseEntity<String> paymentFailure(
            @RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String external_reference) {

        log.warn("❌ Pago fallido - Payment ID: {}, External Ref: {}", payment_id, external_reference);

        return ResponseEntity.ok("El pago no pudo ser procesado. Intenta nuevamente.");
    }

    @GetMapping("/pendiente")
    public ResponseEntity<String> paymentPending(
            @RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String external_reference) {

        log.info("⏳ Pago pendiente - Payment ID: {}, External Ref: {}", payment_id, external_reference);

        return ResponseEntity.ok("Tu pago está siendo procesado. Te notificaremos cuando se complete.");
    }
}