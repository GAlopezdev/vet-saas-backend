package com.veterinaria.service;

import com.veterinaria.dto.request.ServicioRequest;
import com.veterinaria.dto.response.ServicioResponse;
import com.veterinaria.model.entity.ServicioEmpresa;

import java.util.List;

public interface ServicioEmpresaService {

    ServicioResponse crearServicio(ServicioRequest request);

    List<ServicioResponse> listarMisServicios();

    ServicioResponse actualizarServicio(Long idServicio, ServicioRequest request);

}
