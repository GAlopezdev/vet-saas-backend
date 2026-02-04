package com.veterinaria.service;

import com.veterinaria.dto.request.EmpresaProfileRequest;

public interface EmpresaService {

    void actualizarPerfilCompleto(Long idUsuario, EmpresaProfileRequest request);

}
