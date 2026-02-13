package com.veterinaria.service.impl;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.veterinaria.dto.mercadopago.CartItemRequest;
import com.veterinaria.model.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mp.webhook-url}")
    private String webhookUrl;

    @Value("${mp.back-urls.success}")
    private String successUrl;

    @Value("${mp.back-urls.failure}")
    private String failureUrl;

    @Value("${mp.back-urls.pending}")
    private String pendingUrl;

    public Preference createPreference(Order order, List<CartItemRequest> cartItems) {
        try {
            log.info("🔵 Creando preferencia para orden: {}", order.getIdOrden());

            // Convertir items del carrito a items de MP
            List<PreferenceItemRequest> items = cartItems.stream()
                    .map(this::convertToPreferenceItem)
                    .toList();

            // Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .pending(pendingUrl)
                    .build();

            // Crear request de preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(order.getIdOrden().toString())
                    .notificationUrl(webhookUrl)
                    .statementDescriptor("VETERINARIA - Orden #" + order.getIdOrden())
                    .build();

            // Crear la preferencia usando el cliente oficial
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            log.info("✅ Preferencia creada exitosamente: {}", preference.getId());
            log.info("🔗 Init Point: {}", preference.getInitPoint());

            return preference;

        } catch (MPApiException apiException) {
            log.error("❌ Error API de Mercado Pago: {}", apiException.getApiResponse().getContent());
            throw new RuntimeException("Error al crear preferencia en Mercado Pago: " + apiException.getMessage());
        } catch (MPException mpException) {
            log.error("❌ Error SDK de Mercado Pago: {}", mpException.getMessage());
            throw new RuntimeException("Error de comunicación con Mercado Pago: " + mpException.getMessage());
        }
    }

    private PreferenceItemRequest convertToPreferenceItem(CartItemRequest item) {
        return PreferenceItemRequest.builder()
                .title(item.titulo() != null ? item.titulo() : "Item - " + item.tipoItem())
                .description(item.descripcion())
                .quantity(item.cantidad())
                .currencyId("PEN") // Soles peruanos
                .unitPrice(item.precioUnitario())
                .build();
    }

    public BigDecimal calculateOrderTotal(List<CartItemRequest> items) {
        return items.stream()
                .map(item -> item.precioUnitario().multiply(BigDecimal.valueOf(item.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}