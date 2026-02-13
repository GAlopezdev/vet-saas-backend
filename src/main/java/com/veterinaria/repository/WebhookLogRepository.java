package com.veterinaria.repository;

import com.veterinaria.model.entity.WebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebhookLogRepository extends JpaRepository<WebhookLog, String> {

    /**
     * Buscar webhook por payment_id (clave primaria)
     * Útil para verificar idempotencia (si ya se procesó este pago)
     */
    Optional<WebhookLog> findByPaymentId(String paymentId);

    /**
     * Verificar si un payment_id ya existe (para idempotencia)
     */
    boolean existsByPaymentId(String paymentId);

    /**
     * Buscar todos los webhooks de una orden específica
     */
    List<WebhookLog> findByOrdenId(Long ordenId);

    /**
     * Buscar webhooks por estado
     */
    List<WebhookLog> findByEstado(String estado);

    /**
     * Buscar webhooks por request_id
     */
    List<WebhookLog> findByRequestId(String requestId);

    /**
     * Buscar webhooks procesados en un rango de fechas
     */
    List<WebhookLog> findByProcesadoEnBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Contar webhooks por estado
     */
    long countByEstado(String estado);

    /**
     * Buscar webhooks con errores
     */
    List<WebhookLog> findByEstadoAndMensajeErrorIsNotNull(String estado);
}