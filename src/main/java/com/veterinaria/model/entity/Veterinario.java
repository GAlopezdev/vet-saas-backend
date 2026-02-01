package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public Veterinario() {}

    public Veterinario(String especialidad, String telefono, String apema, String apepa, String nombres, Usuario usuario) {
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.apema = apema;
        this.apepa = apepa;
        this.nombres = nombres;
        this.usuario = usuario;
    }
}
