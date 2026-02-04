package com.veterinaria.mapper;

import com.veterinaria.dto.response.ClienteUpdateResponse;
import com.veterinaria.dto.response.EmpresaResponse;
import com.veterinaria.dto.response.VeterinarioResponse;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.PerfilCliente;
import com.veterinaria.model.entity.Veterinario;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    public ClienteUpdateResponse toClienteResponse(PerfilCliente entity) {
        if (entity == null) return null;
        return new ClienteUpdateResponse(
                entity.getIdPerfil(),
                entity.getNombres(),
                entity.getApepa(),
                entity.getApema(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getCiudad(),
                entity.getPais(),
                entity.getFotoPerfil()
        );
    }

    public VeterinarioResponse toVeterinarioResponse(Veterinario entity) {
        if (entity == null) return null;
        return new VeterinarioResponse(
                entity.getIdVeterinario(),
                entity.getNombres(),
                entity.getApepa(),
                entity.getApema(),
                entity.getTelefono(),
                entity.getEspecialidad(),
                entity.getAniosExperiencia(),
                entity.getFotoPerfil()
        );
    }

    public EmpresaResponse toEmpresaResponse(Empresa entity) {
        if (entity == null) return null;
        return new EmpresaResponse(
                entity.getIdEmpresa(),
                entity.getNombreComercial(),
                entity.getDescripcion(),
                entity.getHorarioAtencion(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getCiudad(),
                entity.getPais(),
                entity.getLogoUrl(),
                entity.getBannerUrl(),
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getTipoEmpresa() != null ? entity.getTipoEmpresa().getDescripcion() : null
        );
    }

}
