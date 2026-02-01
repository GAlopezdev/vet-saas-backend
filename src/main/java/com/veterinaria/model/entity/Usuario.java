package com.veterinaria.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.veterinaria.model.enums.UserRol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRol rol;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Column(name = "email_verificado", nullable = false)
    private boolean emailVerificado;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Usuario() {
        this.estado = true;
        this.emailVerificado = false;
    }

    public Usuario(String correo, String contrasenia, UserRol rol) {
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.estado = true;
        this.emailVerificado = false;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estado;
    }
}
