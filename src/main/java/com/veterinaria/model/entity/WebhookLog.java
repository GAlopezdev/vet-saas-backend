package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookLog {

    @Id
    @Column(name = "payment_id", length = 100, nullable = false)
    private String paymentId;

    @Column(name = "procesado_en", nullable = false)
    private LocalDateTime procesadoEn;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "orden_id")
    private Long ordenId;

    @Column(name = "request_id", length = 200)
    private String requestId;

    @Column(name = "intentos", nullable = false)
    @Builder.Default
    private Integer intentos = 1;

    @Column(name = "mensaje_error", columnDefinition = "TEXT")
    private String mensajeError;

    @PrePersist
    protected void onCreate() {
        if (this.procesadoEn == null) {
            this.procesadoEn = LocalDateTime.now();
        }
        if (this.intentos == null) {
            this.intentos = 1;
        }
    }

    /**
     * Estados posibles del webhook:
     * - PROCESADO: Webhook procesado exitosamente
     * - ERROR: Error al procesar webhook
     * - DUPLICADO: Webhook ya fue procesado anteriormente
     * - PENDIENTE: Webhook recibido pero pendiente de procesar
     * - NO_MANEJADO: Webhook recibido pero no es de tipo payment
     * - RECHAZADO: Pago rechazado
     * - FRAUDE: Pago marcado como fraudulento
     */
    public static class Estado {
        public static final String PROCESADO = "PROCESADO";
        public static final String ERROR = "ERROR";
        public static final String DUPLICADO = "DUPLICADO";
        public static final String PENDIENTE = "PENDIENTE";
        public static final String NO_MANEJADO = "NO_MANEJADO";
        public static final String RECHAZADO = "RECHAZADO";
        public static final String FRAUDE = "FRAUDE";
    }
}