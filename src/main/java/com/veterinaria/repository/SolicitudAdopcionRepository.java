package com.veterinaria.repository;

import com.veterinaria.model.entity.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {
    // Para que el dueño vea las solicitudes de su publicación
    List<SolicitudAdopcion> findByPublicacionIdAdopcion(Long publicacionId);

    // Para que un usuario vea sus propias solicitudes enviadas
    List<SolicitudAdopcion> findByInteresadoUsuarioId(Long interesadoUsuarioId);
}