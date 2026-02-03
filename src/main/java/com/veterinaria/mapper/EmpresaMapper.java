package com.veterinaria.mapper;

import com.veterinaria.dto.response.EmpresaSummaryResponse;
import com.veterinaria.model.entity.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public EmpresaSummaryResponse toSummaryResponse(Empresa e) {
        if (e == null) return null;
        return new EmpresaSummaryResponse(
                e.getIdEmpresa(),
                e.getNombreComercial(),
                e.getRuc(),
                e.getRazonSocial(),
                e.getUsuario().getCorreo(),
                e.getTelefono(),
                e.getPais(),
                e.getEstadoRegistro(),
                e.getCreatedAt()
        );
    }
}
