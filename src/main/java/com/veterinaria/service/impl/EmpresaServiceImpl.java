package com.veterinaria.service.impl;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.HorarioEmpresa;
import com.veterinaria.repository.EmpresaRepository;
import com.veterinaria.repository.HorarioEmpresaRepository;
import com.veterinaria.service.EmpresaService;
import com.veterinaria.validation.EmpresaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepo;
    private final HorarioEmpresaRepository horarioRepo;
    private final EmpresaValidator validator;

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public void actualizarPerfilCompleto(Long idUsuario, EmpresaProfileRequest request) {
        Empresa empresa = empresaRepo.findByUsuarioCorreo(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if (validator.existeNombreComercial(request.nombreComercial(), empresa.getIdEmpresa())) {
            throw new IllegalArgumentException("El nombre comercial ya está en uso por otra empresa.");
        }

        validator.validarDiasDuplicados(request.horarios());

        empresa.setNombreComercial(request.nombreComercial());
        empresa.setTelefono(request.telefono());
        empresa.setDireccion(request.direccion());
        empresa.setDescripcion(request.descripcion());

        List<HorarioEmpresa> actuales = horarioRepo.findByEmpresaIdEmpresaOrderByDiaSemanaAsc(empresa.getIdEmpresa());
        horarioRepo.deleteAll(actuales);

        List<HorarioEmpresa> nuevosHorarios = request.horarios().stream().map(h -> {
            validator.validarCoherenciaHoras(h);

            HorarioEmpresa horario = new HorarioEmpresa();
            horario.setEmpresa(empresa);
            horario.setDiaSemana(h.diaSemana());

            if (h.horaApertura() != null && !h.horaApertura().isBlank()) {
                horario.setHoraApertura(LocalTime.parse(h.horaApertura()));
            }
            if (h.horaCierre() != null && !h.horaCierre().isBlank()) {
                horario.setHoraCierre(LocalTime.parse(h.horaCierre()));
            }

            horario.setEstaCerrado(h.estaCerrado());
            return horario;
        }).toList();

        horarioRepo.saveAll(nuevosHorarios);
        empresaRepo.save(empresa);
    }
}