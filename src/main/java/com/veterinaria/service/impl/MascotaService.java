package com.veterinaria.service.impl;

import com.veterinaria.dto.request.MascotaRequest;
import com.veterinaria.dto.response.MascotaResponse;
import com.veterinaria.model.entity.Mascota;
import com.veterinaria.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public Mascota registrarMascota(MascotaRequest request, MultipartFile imagen) {
        String urlImagen = request.fotoUrl(); // URL por defecto si existe

        // 1. Subir imagen a Cloudinary si el archivo no está vacío
        if (imagen != null && !imagen.isEmpty()) {
            try {
                // Usamos tu método uploadImage y lo guardamos en la carpeta 'mascotas'
                urlImagen = cloudinaryService.uploadImage(imagen, "mascotas");
            } catch (IOException e) {
                // Manejo de error: podrías lanzar una excepción personalizada aquí
                throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
            }
        }

        // 2. Construir la entidad con la URL obtenida
        Mascota mascota = Mascota.builder()
                .usuarioId(request.usuarioId())
                .nombre(request.nombre())
                .especie(request.especie())
                .raza(request.raza())
                .sexo(request.sexo())
                .fechaNacimiento(request.fechaNacimiento())
                .peso(request.peso())
                .color(request.color())
                .fotoUrl(urlImagen) // Aquí asignamos la URL de Cloudinary
                .observaciones(request.observaciones())
                .estado(true)
                .build();

        return mascotaRepository.save(mascota);
    }

    @Transactional(readOnly = true)
    public List<MascotaResponse> listarMascotasPorUsuario(Long usuarioId) {
        return mascotaRepository.findByUsuarioIdAndEstadoTrue(usuarioId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MascotaResponse mapToResponse(Mascota m) {
        return new MascotaResponse(
                m.getIdMascota(),
                m.getNombre(),
                m.getEspecie(),
                m.getRaza(),
                m.getSexo(),
                m.getFechaNacimiento(),
                m.getPeso(),
                m.getColor(),
                m.getFotoUrl(),
                m.getObservaciones(),
                m.getEstado()
        );
    }


}
