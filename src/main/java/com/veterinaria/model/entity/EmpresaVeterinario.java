package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "empresa_veterinarios")
public class EmpresaVeterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresaVet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(length = 100)
    private String cargo;

    @Column(name = "estado_vinculo", length = 20)
    private String estadoVinculo = "ACTIVO";

    @CreationTimestamp
    @Column(name = "fecha_vinculo", updatable = false)
    private LocalDateTime fechaVinculo;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
