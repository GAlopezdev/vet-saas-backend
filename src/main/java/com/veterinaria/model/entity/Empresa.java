package com.veterinaria.model.entity;

import com.veterinaria.model.enums.EstadoRegistroEmpresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_empresa_id", nullable = false)
    private TipoEmpresa tipoEmpresa;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "horario_atencion", length = 100)
    private String horarioAtencion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 100)
    private String pais;

    @Column(length = 200)
    private String direccion;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitud;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitud;

    @Column(name = "ruc", length = 11, unique = true)
    private String ruc;

    @Column(name = "razon_social", length = 200)
    private String razonSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", length = 20)
    private EstadoRegistroEmpresa estadoRegistro;

    @Column(name = "verificado_at")
    private LocalDateTime verificadoAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Empresa() {}

    public Empresa(Usuario usuario, TipoEmpresa tipo, String nombreComercial,
                   String ruc, String razonSocial, String telefono,
                   String pais, String ciudad, String direccion) {
        this.usuario = usuario;
        this.tipoEmpresa = tipo;
        this.nombreComercial = nombreComercial;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.estadoRegistro = EstadoRegistroEmpresa.PENDIENTE;
    }
}
