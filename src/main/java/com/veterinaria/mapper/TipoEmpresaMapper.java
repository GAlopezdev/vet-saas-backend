package com.veterinaria.mapper;

import com.veterinaria.dto.response.TipoEmpresaResponse;
import com.veterinaria.model.entity.TipoEmpresa;
import org.springframework.stereotype.Component;

@Component
public class TipoEmpresaMapper {

    public TipoEmpresaResponse toResponse(TipoEmpresa entity) {
        if (entity == null) return null;

        return new TipoEmpresaResponse(
                entity.getIdTipoEmpresa(),
                entity.getDescripcion()
        );
    }

}
