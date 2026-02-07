package com.veterinaria.service;

import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.request.VincularVeterinarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.dto.response.VeterinarioAsociadoResponse;
import com.veterinaria.dto.response.VeterinarioBusquedaResponse;

import java.util.List;

public interface EmpresaService {

    List<HorarioResponse> actualizarHorarios(List<HorarioRequest> requests);

    List<HorarioResponse> listarMisHorarios();

    void asociarVeterinario(VincularVeterinarioRequest request);

    List<VeterinarioAsociadoResponse> listarVeterinariosAsociados();

    List<VeterinarioBusquedaResponse> buscarVeterinariosParaAsociar(String filtro);

    void desvincularVeterinario(Long idVeterinario);
}
