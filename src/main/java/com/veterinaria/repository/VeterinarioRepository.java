package com.veterinaria.repository;

import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByUsuarioCorreo(String currentUserEmail);

    List<Veterinario> findByEstadoRegistro(EstadoRegistro estado);

}
