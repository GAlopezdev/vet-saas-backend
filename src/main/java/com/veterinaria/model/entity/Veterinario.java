package com.veterinaria.model.entity;

import com.veterinaria.model.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeterinario;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private String nombres;
    private String apepa;
    private String apema;
    private String telefono;
    private String especialidad;
    private Integer aniosExperiencia;
    private String fotoPerfil;

    @Column(name = "numero_colegiatura", unique = true)
    private String numeroColegiatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro")
    private EstadoRegistro estadoRegistro;

    @Column(name = "verificado_at")
    private LocalDateTime verificadoAt;

    @ManyToOne
    @JoinColumn(name = "verificado_por")
    private Usuario verificadoPor;

    public Veterinario() {}

    public Veterinario(Usuario usuario, String nombres, String apepa, String apema,
                       String telefono, String especialidad, Integer aniosExperiencia, String numeroColegiatura) {
        this.usuario = usuario;
        this.nombres = nombres;
        this.apepa = apepa;
        this.apema = apema;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
        this.numeroColegiatura = numeroColegiatura;
        this.estadoRegistro = EstadoRegistro.PENDIENTE;
    }
}
