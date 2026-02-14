package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicaciones_adopcion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adopcion")
    private Long idAdopcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // Quien publica (dueño o empresa)

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion_historia", columnDefinition = "TEXT")
    private String descripcionHistoria;

    @Column(name = "requisitos_adopcion", columnDefinition = "TEXT")
    private String requisitosAdopcion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoAdopcion estado; // DISPONIBLE, CERRADA

    @Column(name = "fecha_publicacion", insertable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    public enum EstadoAdopcion {
        DISPONIBLE, CERRADA
    }
}