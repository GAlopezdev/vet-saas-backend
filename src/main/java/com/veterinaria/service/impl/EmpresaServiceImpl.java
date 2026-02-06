package com.veterinaria.service.impl;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.mapper.HorarioEmpresaMapper;
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
    private final HorarioEmpresaMapper horarioMapper;

    private Empresa getAuthenticatedEmpresa() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return empresaRepo.findByUsuarioCorreo(email)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    @Override
    @Transactional
    public List<HorarioResponse> actualizarHorarios(List<HorarioRequest> requests) {
        Empresa empresa = getAuthenticatedEmpresa();

        validator.validarDiasDuplicados(requests);
        requests.forEach(validator::validarCoherenciaHoras);

        List<HorarioEmpresa> horariosActuales = horarioRepo.findAllByEmpresaIdEmpresa(empresa.getIdEmpresa());

        List<HorarioEmpresa> horariosParaGuardar = requests.stream().map(dto -> {
            HorarioEmpresa entidad = horariosActuales.stream()
                    .filter(h -> h.getDiaSemana().equals(dto.diaSemana()))
                    .findFirst()
                    .orElse(new HorarioEmpresa());

            if (entidad.getIdHorario() == null) {
                entidad.setEmpresa(empresa);
                entidad.setDiaSemana(dto.diaSemana());
            }

            entidad.setEstaCerrado(dto.estaCerrado());
            if (dto.estaCerrado()) {
                entidad.setHoraApertura(null);
                entidad.setHoraCierre(null);
            } else {
                entidad.setHoraApertura(LocalTime.parse(dto.horaApertura()));
                entidad.setHoraCierre(LocalTime.parse(dto.horaCierre()));
            }

            return entidad;
        }).toList();

        List<Integer> diasQueVienen = requests.stream().map(HorarioRequest::diaSemana).toList();
        List<HorarioEmpresa> paraEliminar = horariosActuales.stream()
                .filter(h -> !diasQueVienen.contains(h.getDiaSemana()))
                .toList();

        if (!paraEliminar.isEmpty()) {
            horarioRepo.deleteAll(paraEliminar);
        }

        return horarioRepo.saveAll(horariosParaGuardar).stream()
                .map(horarioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listarMisHorarios() {
        Empresa empresa = getAuthenticatedEmpresa();
        return horarioRepo.findAllByEmpresaIdEmpresa(empresa.getIdEmpresa()).stream()
                .map(h -> new HorarioResponse(h.getIdHorario(), h.getDiaSemana(), h.getHoraApertura(), h.getHoraCierre(), h.isEstaCerrado()))
                .toList();
    }
}