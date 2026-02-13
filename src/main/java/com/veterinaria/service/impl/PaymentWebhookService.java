package com.veterinaria.service.impl;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.veterinaria.dto.mercadopago.WebhookNotification;
import com.veterinaria.model.entity.WebhookLog;
import com.veterinaria.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final OrderService orderService;
    private final WebhookLogRepository webhookLogRepository;

    @Transactional
    public void processWebhook(WebhookNotification notification) {
        String paymentId = null;
        if (notification.data() != null && notification.data().id() != null) {
            paymentId = notification.data().id();
        } else if (notification.id() != null) {
            paymentId = notification.id().toString();
        }

        String type = notification.type();
        log.info("🔔 Webhook procesando - Tipo: {}, ID: {}, Action: {}", type, paymentId, notification.action());

        if (type == null || paymentId == null) {
            log.warn("⚠️ Webhook ignorado: Faltan datos esenciales (Tipo o ID nulos)");
            return;
        }

        if (!type.contains("payment")) {
            if (type.contains("merchant_order")) {
                log.info("📦 Merchant Order ({}) recibida. Ignorando a la espera del evento 'payment'.", paymentId);
            } else {
                log.info("⏭️ Tipo de evento ignorado: {}", type);
            }
            return;
        }

        if (webhookLogRepository.existsByPaymentId(paymentId)) {
            log.warn("⚠️ Webhook DUPLICADO para payment_id: {} - Ignorando", paymentId);
            webhookLogRepository.findByPaymentId(paymentId).ifPresent(log -> {
                log.setIntentos(log.getIntentos() + 1);
                webhookLogRepository.save(log);
            });
            return;
        }

        WebhookLog webhookLog = WebhookLog.builder()
                .paymentId(paymentId)
                .procesadoEn(LocalDateTime.now())
                .estado(WebhookLog.Estado.PENDIENTE)
                .intentos(1)
                .build();

        try {
            log.info("💳 Consultando detalles del pago {} en Mercado Pago...", paymentId);
            Payment payment = getPaymentInfo(paymentId);

            String status = payment.getStatus();
            String statusDetail = payment.getStatusDetail();
            String externalReference = payment.getExternalReference();
            Long merchantOrderId = (payment.getOrder() != null) ? payment.getOrder().getId() : null;

            webhookLog.setPaymentStatus(status);
            webhookLog.setRequestId(merchantOrderId != null ? merchantOrderId.toString() : null);

            if (externalReference == null || externalReference.isBlank()) {
                throw new IllegalStateException("El pago no posee external_reference (ID de Orden)");
            }

            Long orderId = Long.parseLong(externalReference);
            webhookLog.setOrdenId(orderId);

            updateProcessStatus(status, statusDetail, webhookLog);

            orderService.updatePaymentStatus(
                    orderId,
                    paymentId,
                    status,
                    merchantOrderId != null ? merchantOrderId.toString() : null
            );

            log.info("✅ Pago procesado - Orden: {}, Estado: {}", orderId, status);

        } catch (Exception e) {
            log.error("❌ Error en procesamiento: {}", e.getMessage());
            webhookLog.setEstado(WebhookLog.Estado.ERROR);
            webhookLog.setMensajeError(e.getMessage());
        } finally {
            webhookLogRepository.save(webhookLog);
        }
    }

    private void updateProcessStatus(String status, String statusDetail, WebhookLog logEntity) {
        if ("cc_rejected_high_risk".equals(statusDetail) || "rejected_high_risk".equals(statusDetail)) {
            logEntity.setEstado(WebhookLog.Estado.FRAUDE);
        } else if ("rejected".equals(status) || "cancelled".equals(status)) {
            logEntity.setEstado(WebhookLog.Estado.RECHAZADO);
        } else if ("approved".equals(status)) {
            logEntity.setEstado(WebhookLog.Estado.PROCESADO);
        } else {
            logEntity.setEstado(WebhookLog.Estado.PENDIENTE);
        }
    }

    private Payment getPaymentInfo(String paymentId) {
        try {
            return new PaymentClient().get(Long.parseLong(paymentId));
        } catch (MPApiException e) {
            log.error("❌ Error API MP (Status {}): {}", e.getApiResponse().getStatusCode(), e.getApiResponse().getContent());
            throw new RuntimeException("Error consultando pago en Mercado Pago");
        } catch (MPException e) {
            throw new RuntimeException("Error de comunicación con el SDK de MP");
        }
    }
}