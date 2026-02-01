package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "perfiles_clientes")
public class PerfilCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfil;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private String nombres;
    private String apepa;
    private String apema;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String fotoPerfil;

    public PerfilCliente() {}

    public PerfilCliente(Usuario usuario, String nombres, String apepa, String apema, String telefono, String pais) {
        this.usuario = usuario;
        this.nombres = nombres;
        this.apepa = apepa;
        this.apema = apema;
        this.telefono = telefono;
        this.pais = pais;
    }
}
