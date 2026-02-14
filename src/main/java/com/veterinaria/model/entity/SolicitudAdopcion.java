package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_adopcion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private PublicacionAdopcion publicacion;

    @Column(name = "interesado_usuario_id", nullable = false)
    private Long interesadoUsuarioId;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_solicitud", insertable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    public enum EstadoSolicitud {
        PENDIENTE, APROBADA, RECHAZADA
    }
}