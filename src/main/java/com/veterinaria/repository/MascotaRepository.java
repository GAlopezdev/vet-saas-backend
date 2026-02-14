package com.veterinaria.repository;

import com.veterinaria.model.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // Buscar mascotas por dueño (activo = true por defecto)
    List<Mascota> findByUsuarioIdAndEstadoTrue(Long usuarioId);

    // Buscar por especie (útil para filtros en adopción)
    List<Mascota> findByEspecieIgnoreCase(String especie);
}