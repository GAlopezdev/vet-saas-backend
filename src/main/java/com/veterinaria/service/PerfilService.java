package com.veterinaria.service;

import com.veterinaria.dto.request.ClienteUpdateRequest;
import com.veterinaria.dto.request.EmpresaUpdateRequest;
import com.veterinaria.dto.request.VeterinarioUpdateRequest;
import com.veterinaria.dto.response.ClienteUpdateResponse;
import com.veterinaria.dto.response.EmpresaResponse;
import com.veterinaria.dto.response.VeterinarioResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PerfilService {

    ClienteUpdateResponse patchCliente(ClienteUpdateRequest dto, MultipartFile imagen);

    VeterinarioResponse patchVeterinario(VeterinarioUpdateRequest dto, MultipartFile imagen);

    EmpresaResponse patchEmpresa(EmpresaUpdateRequest dto, MultipartFile logo, MultipartFile banner);

    Object obtenerDatosCompletos(String correo);
}
