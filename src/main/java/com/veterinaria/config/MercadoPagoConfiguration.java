package com.veterinaria.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MercadoPagoConfiguration {

    @Value("${mp.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            log.info("✅ Mercado Pago configurado correctamente");
        } catch (Exception e) {
            log.error("❌ Error configurando Mercado Pago: {}", e.getMessage());
            throw new RuntimeException("Error al configurar Mercado Pago", e);
        }
    }
}