package com.veterinaria.service.impl;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.request.VincularVeterinarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.dto.response.VeterinarioAsociadoResponse;
import com.veterinaria.dto.response.VeterinarioBusquedaResponse;
import com.veterinaria.mapper.EmpresaMapper;
import com.veterinaria.mapper.HorarioEmpresaMapper;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.EmpresaVeterinario;
import com.veterinaria.model.entity.HorarioEmpresa;
import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.repository.EmpresaRepository;
import com.veterinaria.repository.EmpresaVeterinarioRepository;
import com.veterinaria.repository.HorarioEmpresaRepository;
import com.veterinaria.repository.VeterinarioRepository;
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
    private final VeterinarioRepository veterinarioRepo;
    private final EmpresaVeterinarioRepository empresaVetRepo;
    private final EmpresaMapper empresaMapper;

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

    @Override
    @Transactional
    public void asociarVeterinario(VincularVeterinarioRequest request) {
        Empresa empresa = getAuthenticatedEmpresa();

        Veterinario veterinario = veterinarioRepo.findById(request.veterinarioId())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        if (empresaVetRepo.existsByEmpresaAndVeterinario(empresa, veterinario)) {
            throw new RuntimeException("Este veterinario ya está asociado a tu empresa");
        }

        EmpresaVeterinario vinculo = new EmpresaVeterinario();
        vinculo.setEmpresa(empresa);
        vinculo.setVeterinario(veterinario);
        vinculo.setCargo(request.cargo());

        empresaVetRepo.save(vinculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinarioAsociadoResponse> listarVeterinariosAsociados() {
        Empresa empresa = getAuthenticatedEmpresa();

        return empresaVetRepo.findAllByEmpresaIdEmpresa(empresa.getIdEmpresa())
                .stream()
                .map(empresaMapper::toVeterinarioAsociadoResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinarioBusquedaResponse> buscarVeterinariosParaAsociar(String filtro) {
        Empresa empresa = getAuthenticatedEmpresa();

        // Pasamos el ID de la empresa actual para que el Query de SQL los excluya
        return veterinarioRepo.buscarVeterinariosNoVinculados(filtro, empresa.getIdEmpresa())
                .stream()
                .map(v -> new VeterinarioBusquedaResponse(
                        v.getIdVeterinario(),
                        v.getNombres() + " " + v.getApepa() + " " + (v.getApema() != null ? v.getApema() : ""),
                        v.getEspecialidad(),
                        v.getAniosExperiencia(),
                        v.getFotoPerfil(),
                        v.getNumeroColegiatura()
                )).toList();
    }

    // EmpresaServiceImpl.java

    @Override
    @Transactional
    public void desvincularVeterinario(Long idVeterinario) {
        Empresa empresa = getAuthenticatedEmpresa();

        if (!empresaVetRepo.existsByEmpresaIdEmpresaAndVeterinarioIdVeterinario(
                empresa.getIdEmpresa(), idVeterinario)) {
            throw new RuntimeException("El veterinario no pertenece al staff de esta empresa");
        }

        empresaVetRepo.deleteByEmpresaIdEmpresaAndVeterinarioIdVeterinario(
                empresa.getIdEmpresa(), idVeterinario);
    }
}