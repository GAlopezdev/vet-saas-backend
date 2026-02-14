package com.veterinaria.service.impl;

import com.veterinaria.dto.request.AdopcionRequest;
import com.veterinaria.dto.request.DecisionAdopcionRequest;
import com.veterinaria.dto.request.SolicitudAdopcionRequest;
import com.veterinaria.dto.response.AdopcionResponse;
import com.veterinaria.dto.response.SolicitudDetalleResponse;
import com.veterinaria.model.entity.Mascota;
import com.veterinaria.model.entity.PublicacionAdopcion;
import com.veterinaria.model.entity.SolicitudAdopcion;
import com.veterinaria.repository.AdopcionRepository;
import com.veterinaria.repository.MascotaRepository;
import com.veterinaria.repository.SolicitudAdopcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;
    private final MascotaRepository mascotaRepository;
    private final SolicitudAdopcionRepository solicitudRepository;

    @Transactional
    public AdopcionResponse publicar(AdopcionRequest request) {
        var mascota = mascotaRepository.findById(request.mascotaId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        PublicacionAdopcion nueva = PublicacionAdopcion.builder()
                .mascota(mascota)
                .usuarioId(request.usuarioId())
                .titulo(request.titulo())
                .descripcionHistoria(request.historia())
                .requisitosAdopcion(request.requisitos())
                .estado(PublicacionAdopcion.EstadoAdopcion.DISPONIBLE)
                .fechaPublicacion(LocalDateTime.now())
                .build();

        var guardada = adopcionRepository.save(nueva);
        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<AdopcionResponse> listarDisponibles() {
        return adopcionRepository.findByEstadoOrderByFechaPublicacionDesc(PublicacionAdopcion.EstadoAdopcion.DISPONIBLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public String enviarSolicitud(SolicitudAdopcionRequest request) {
        var publicacion = adopcionRepository.findById(request.publicacionId())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        if (publicacion.getEstado() == PublicacionAdopcion.EstadoAdopcion.CERRADA) {
            throw new RuntimeException("Esta publicación ya no acepta más solicitudes.");
        }

        SolicitudAdopcion solicitud = SolicitudAdopcion.builder()
                .publicacion(publicacion)
                .interesadoUsuarioId(request.interesadoUsuarioId())
                .mensaje(request.mensaje())
                .build();

        solicitudRepository.save(solicitud);
        return "Solicitud enviada con éxito. El anunciante se pondrá en contacto contigo.";
    }

    @Transactional(readOnly = true)
    public List<SolicitudDetalleResponse> listarInteresados(Long publicacionId) {
        // Verificamos que la publicación exista
        if (!adopcionRepository.existsById(publicacionId)) {
            throw new RuntimeException("Publicación no encontrada");
        }

        return solicitudRepository.findByPublicacionIdAdopcion(publicacionId)
                .stream()
                .map(s -> new SolicitudDetalleResponse(
                        s.getIdSolicitud(),
                        s.getInteresadoUsuarioId(),
                        s.getMensaje(),
                        s.getEstado(),
                        s.getFechaSolicitud()
                ))
                .toList();
    }

    @Transactional
    public String procesarDecision(Long solicitudId, DecisionAdopcionRequest request) {
        // 1. Buscar la solicitud
        SolicitudAdopcion solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // 2. Si se aprueba, cerramos todo
        if (request.nuevoEstado() == SolicitudAdopcion.EstadoSolicitud.APROBADA) {

            // A. Actualizar solicitud
            solicitud.setEstado(SolicitudAdopcion.EstadoSolicitud.APROBADA);

            // B. Cerrar la publicación
            PublicacionAdopcion pub = solicitud.getPublicacion();
            pub.setEstado(PublicacionAdopcion.EstadoAdopcion.CERRADA);

            // C. Desactivar la mascota (ya no está disponible para nadie más)
            Mascota mascota = pub.getMascota();
            mascota.setEstado(false);

            adopcionRepository.save(pub);
            mascotaRepository.save(mascota);

            solicitudRepository.save(solicitud);
            return "Adopción concretada con éxito. La publicación ha sido cerrada.";
        }

        // 3. Si se rechaza, solo cambiamos el estado de esa solicitud específica
        if (request.nuevoEstado() == SolicitudAdopcion.EstadoSolicitud.RECHAZADA) {
            solicitud.setEstado(SolicitudAdopcion.EstadoSolicitud.RECHAZADA);
            solicitudRepository.save(solicitud);
            return "La solicitud ha sido rechazada.";
        }

        return "Estado actualizado.";
    }

    private AdopcionResponse mapToResponse(PublicacionAdopcion p) {
        return new AdopcionResponse(
                p.getIdAdopcion(),
                p.getTitulo(),
                p.getMascota().getNombre(),
                p.getMascota().getEspecie(),
                p.getMascota().getFotoUrl(),
                p.getDescripcionHistoria(),
                p.getFechaPublicacion()
        );
    }
}