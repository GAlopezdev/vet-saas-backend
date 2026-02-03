package com.veterinaria.service;

import com.veterinaria.dto.response.EmpresaSummaryResponse;
import com.veterinaria.dto.response.VeterinarioSummaryResponse;
import com.veterinaria.model.entity.Usuario;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    void aprobarVeterinario(Long idVeterinario, Usuario admin);
    void rechazarVeterinario(Long idVeterinario, Usuario admin);
    void aprobarEmpresa(Long idEmpresa, Usuario admin);
    void rechazarEmpresa(Long idEmpresa, Usuario admin);

    Page<VeterinarioSummaryResponse> listarVeterinarios(EstadoRegistro estado, Pageable pageable);
    Page<EmpresaSummaryResponse> listarEmpresas(EstadoRegistro estado, Pageable pageable);
}
