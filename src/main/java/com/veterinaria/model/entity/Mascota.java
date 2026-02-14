package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mascotas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long idMascota;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // El dueño (Cliente o Empresa)

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String especie;

    @Column(length = 50)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SexoMascota sexo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(length = 30)
    private String color;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Builder.Default
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SexoMascota {
        MACHO, HEMBRA
    }
}