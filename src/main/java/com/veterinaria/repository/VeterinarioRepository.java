package com.veterinaria.repository;

import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByUsuarioCorreo(String currentUserEmail);

    @EntityGraph(attributePaths = {"usuario"})
    Page<Veterinario> findByEstadoRegistro(EstadoRegistro estado, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario"})
    @Query("SELECT v FROM Veterinario v")
    Page<Veterinario> findAllVeterinarios(Pageable pageable);

}
