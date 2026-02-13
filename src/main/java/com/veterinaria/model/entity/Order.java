package com.veterinaria.model.entity;

import com.veterinaria.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long idOrden;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "empresa_id")
    private Long empresaId;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_orden", nullable = false, length = 50)
    private OrderStatus estadoOrden;

    @Column(name = "preference_id", length = 255)
    private String preferenceId;

    @Column(name = "payment_id_mp", length = 255)
    private String paymentIdMp;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "merchant_order_id", length = 255)
    private String merchantOrderId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Método helper para agregar items
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Enum de estados de orden según Flyway V10
     *
     * IMPORTANTE: Estos estados deben coincidir EXACTAMENTE con el constraint
     * definido en la migración V10 de Flyway
     */
    public enum OrderStatus {
        PENDIENTE,           // Orden creada, esperando pago
        PAGADO,              // Pago confirmado por Mercado Pago
        CANCELADO,           // Orden cancelada por el usuario o el sistema
        FINALIZADO,          // Orden completada (producto entregado/servicio prestado)
        RECHAZADO,           // Pago rechazado por Mercado Pago
        REEMBOLSADO,         // Pago devuelto al cliente
        FRAUDE,              // Pago marcado como fraudulento
        ERROR_PROCESAMIENTO  // Error al procesar el pago
    }

    /**
     * Mapeo de estados de Mercado Pago a estados de Orden
     */
    public static OrderStatus mapFromMercadoPagoStatus(String mpStatus) {
        return switch (mpStatus) {
            case "approved" -> OrderStatus.PAGADO;
            case "rejected", "cancelled" -> OrderStatus.RECHAZADO;
            case "refunded", "charged_back" -> OrderStatus.REEMBOLSADO;
            case "in_process", "pending", "authorized", "in_mediation" -> OrderStatus.PENDIENTE;
            default -> OrderStatus.ERROR_PROCESAMIENTO;
        };
    }
}