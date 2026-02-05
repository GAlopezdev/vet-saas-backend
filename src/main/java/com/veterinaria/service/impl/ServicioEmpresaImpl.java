package com.veterinaria.service.impl;

import com.veterinaria.dto.request.ServicioRequest;
import com.veterinaria.dto.response.ServicioResponse;
import com.veterinaria.mapper.ServicioEmpresaMapper;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.ServicioEmpresa;
import com.veterinaria.repository.EmpresaRepository;
import com.veterinaria.repository.ServicioEmpresaRepository;
import com.veterinaria.service.ServicioEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioEmpresaImpl implements ServicioEmpresaService {

    private final ServicioEmpresaRepository servicioRepo;
    private final EmpresaRepository empresaRepo;
    private final ServicioEmpresaMapper servicioMapper;

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        Empresa empresa = empresaRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        ServicioEmpresa servicio = new ServicioEmpresa();
        servicio.setEmpresa(empresa);
        servicio.setNombreServicio(request.nombreServicio());
        servicio.setDescripcion(request.descripcion());
        servicio.setPrecio(request.precio());
        servicio.setActivo(request.activo() != null ? request.activo() : true);

        return servicioMapper.toResponse(servicioRepo.save(servicio));
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarMisServicios() {
        Empresa empresa = empresaRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        return servicioRepo.findAllByEmpresaId(empresa.getIdEmpresa())
                .stream()
                .map(servicioMapper::toResponse)
                .toList();
    }

    @Transactional
    public ServicioResponse actualizarServicio(Long idServicio, ServicioRequest request) {
        Empresa empresa = empresaRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        ServicioEmpresa servicio = servicioRepo.findByIdServicioAndEmpresaIdEmpresa(idServicio, empresa.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado o acceso denegado"));

        if (request.nombreServicio() != null) servicio.setNombreServicio(request.nombreServicio());
        if (request.descripcion() != null) servicio.setDescripcion(request.descripcion());
        if (request.precio() != null) servicio.setPrecio(request.precio());
        if (request.activo() != null) servicio.setActivo(request.activo());

        return servicioMapper.toResponse(servicioRepo.save(servicio));
    }
}
