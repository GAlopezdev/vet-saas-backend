package com.veterinaria.repository;

import com.veterinaria.model.entity.PublicacionAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdopcionRepository extends JpaRepository<PublicacionAdopcion, Long> {
    // Ver todas las mascotas disponibles para el "Feed"
    List<PublicacionAdopcion> findByEstadoOrderByFechaPublicacionDesc(PublicacionAdopcion.EstadoAdopcion estado);

    // Ver mis publicaciones
    List<PublicacionAdopcion> findByUsuarioId(Long usuarioId);
}