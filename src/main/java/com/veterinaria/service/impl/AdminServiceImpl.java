package com.veterinaria.service.impl;

import com.veterinaria.model.entity.Usuario;
import com.veterinaria.model.entity.Veterinario;
import com.veterinaria.model.enums.EstadoRegistro;
import com.veterinaria.repository.EmpresaRepository;
import com.veterinaria.repository.VeterinarioRepository;
import com.veterinaria.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final VeterinarioRepository veterinarioRepository;
    private final EmpresaRepository empresaRepository;

    @Transactional
    public void aprobarVeterinario(Long idVeterinario, Usuario admin) {
        Veterinario vet = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        vet.setEstadoRegistro(EstadoRegistro.VERIFICADO);
        vet.setVerificadoAt(LocalDateTime.now());
        vet.setVerificadoPor(admin);

        vet.getUsuario().setEstado(true);

        veterinarioRepository.save(vet);
    }

    @Transactional
    public void rechazarVeterinario(Long idVeterinario, Usuario admin) {
        Veterinario vet = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

        vet.setEstadoRegistro(EstadoRegistro.RECHAZADO);
        vet.setVerificadoAt(LocalDateTime.now());
        vet.setVerificadoPor(admin);

        vet.getUsuario().setEstado(false);

        veterinarioRepository.save(vet);
    }

    @Transactional
    public void aprobarEmpresa(Long idEmpresa, Usuario admin) {
        var empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        empresa.setEstadoRegistro(EstadoRegistro.VERIFICADO);
        empresa.setVerificadoAt(LocalDateTime.now());
        empresa.setVerificadoPor(admin);

        empresa.getUsuario().setEstado(true);

        empresaRepository.save(empresa);
    }

    @Transactional
    public void rechazarEmpresa(Long idEmpresa, Usuario admin) {
        var empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        empresa.setEstadoRegistro(EstadoRegistro.RECHAZADO);
        empresa.setVerificadoAt(LocalDateTime.now());
        empresa.setVerificadoPor(admin);

        empresa.getUsuario().setEstado(false);

        empresaRepository.save(empresa);
    }
}
