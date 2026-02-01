package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tipo_empresa")
public class TipoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoEmpresa;

    @Column(name = "nombre")
    private String descripcion;

    public TipoEmpresa(){
    }

}
