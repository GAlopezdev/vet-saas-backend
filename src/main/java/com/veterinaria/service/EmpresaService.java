package com.veterinaria.service;

import com.veterinaria.dto.request.EmpresaProfileRequest;
import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.response.HorarioResponse;

import java.util.List;

public interface EmpresaService {

    List<HorarioResponse> actualizarHorarios(List<HorarioRequest> requests);

    List<HorarioResponse> listarMisHorarios();

}
