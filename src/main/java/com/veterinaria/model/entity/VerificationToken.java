package com.veterinaria.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime fechaExpiracion;

    public VerificationToken() {
    }

    public VerificationToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.fechaExpiracion = LocalDateTime.now().plusHours(1);
    }

}
