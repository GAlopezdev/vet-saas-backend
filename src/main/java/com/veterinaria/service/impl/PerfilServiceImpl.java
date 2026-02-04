package com.veterinaria.service.impl;

import com.veterinaria.dto.request.ClienteUpdateRequest;
import com.veterinaria.dto.request.EmpresaUpdateRequest;
import com.veterinaria.dto.request.VeterinarioUpdateRequest;
import com.veterinaria.dto.response.ClienteUpdateResponse;
import com.veterinaria.dto.response.EmpresaResponse;
import com.veterinaria.dto.response.VeterinarioResponse;
import com.veterinaria.mapper.PerfilMapper;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.PerfilCliente;
import com.veterinaria.model.entity.TipoEmpresa;
import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.repository.EmpresaRepository;
import com.veterinaria.repository.PerfilClienteRepository;
import com.veterinaria.repository.TipoEmpresaRepository;
import com.veterinaria.repository.VeterinarioRepository;
import com.veterinaria.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final PerfilClienteRepository clienteRepo;
    private final VeterinarioRepository veterinarioRepo;
    private final EmpresaRepository empresaRepo;
    private final TipoEmpresaRepository tipoRepo;
    private final PerfilMapper mapper;
    private final CloudinaryService cloudinaryService;

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    @Transactional
    public ClienteUpdateResponse patchCliente(ClienteUpdateRequest dto, MultipartFile imagen) {
        PerfilCliente perfil = clienteRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if (dto.nombres() != null && !dto.nombres().isBlank()) perfil.setNombres(dto.nombres());
        if (dto.apepa() != null) perfil.setApepa(dto.apepa());
        if (dto.apema() != null) perfil.setApema(dto.apema());
        if (dto.telefono() != null) perfil.setTelefono(dto.telefono());
        if (dto.direccion() != null) perfil.setDireccion(dto.direccion());
        if (dto.ciudad() != null) perfil.setCiudad(dto.ciudad());
        if (dto.pais() != null) perfil.setPais(dto.pais());
        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = cloudinaryService.uploadImage(imagen, "clientes");
                perfil.setFotoPerfil(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen");
            }
        }

        PerfilCliente actualizado = clienteRepo.save(perfil);
        return mapper.toClienteResponse(actualizado);
    }

    @Override
    @Transactional
    public VeterinarioResponse patchVeterinario(VeterinarioUpdateRequest dto, MultipartFile imagen) {
        Veterinario perfil = veterinarioRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if (dto.nombres() != null && !dto.nombres().isBlank()) perfil.setNombres(dto.nombres());
        if (dto.apepa() != null) perfil.setApepa(dto.apepa());
        if (dto.apema() != null) perfil.setApema(dto.apema());
        if (dto.telefono() != null) perfil.setTelefono(dto.telefono());
        if (dto.especialidad() != null) perfil.setEspecialidad(dto.especialidad());
        if (dto.aniosExperiencia() != null) perfil.setAniosExperiencia(dto.aniosExperiencia());
        if (imagen != null && !imagen.isEmpty()) {
            try {
                String url = cloudinaryService.uploadImage(imagen, "clientes");
                perfil.setFotoPerfil(url);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen");
            }
        }

        Veterinario actualizado = veterinarioRepo.save(perfil);
        return mapper.toVeterinarioResponse(actualizado);
    }

    @Override
    @Transactional
    public EmpresaResponse patchEmpresa(EmpresaUpdateRequest dto, MultipartFile logo, MultipartFile banner) {
        Empresa perfil = empresaRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if (dto.nombreComercial() != null && !dto.nombreComercial().isBlank()) perfil.setNombreComercial(dto.nombreComercial());
        if (dto.descripcion() != null) perfil.setDescripcion(dto.descripcion());
        if (dto.horarioAtencion() != null) perfil.setHorarioAtencion(dto.horarioAtencion());
        if (dto.telefono() != null) perfil.setTelefono(dto.telefono());
        if (dto.direccion() != null) perfil.setDireccion(dto.direccion());
        if (dto.latitud() != null) perfil.setLatitud(dto.latitud());
        if (dto.longitud() != null) perfil.setLongitud(dto.longitud());
        if (logo != null && !logo.isEmpty()) {
            try {
                perfil.setLogoUrl(cloudinaryService.uploadImage(logo, "empresas/logos"));
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el logo");
            }
        }

        // Procesar Banner
        if (banner != null && !banner.isEmpty()) {
            try {
                perfil.setBannerUrl(cloudinaryService.uploadImage(banner, "empresas/banners"));
            } catch (IOException e) {
                throw new RuntimeException("Error al subir el banner");
            }
        }

        if (dto.idTipoEmpresa() != null) {
            TipoEmpresa tipo = tipoRepo.findById(dto.idTipoEmpresa())
                    .orElseThrow(() -> new RuntimeException("Tipo de empresa no encontrado"));
            perfil.setTipoEmpresa(tipo);
        }

        Empresa actualizado = empresaRepo.save(perfil);
        return mapper.toEmpresaResponse(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Object obtenerDatosCompletos(String correo) {

        var cliente = clienteRepo.findByUsuarioCorreo(correo);
        if (cliente.isPresent()) return mapper.toClienteResponse(cliente.get());

        var vet = veterinarioRepo.findByUsuarioCorreo(correo);
        if (vet.isPresent()) return mapper.toVeterinarioResponse(vet.get());

        var empresa = empresaRepo.findByUsuarioCorreo(correo);
        if (empresa.isPresent()) return mapper.toEmpresaResponse(empresa.get());

        throw new RuntimeException("No se encontró perfil para el usuario: " + correo);
    }
}