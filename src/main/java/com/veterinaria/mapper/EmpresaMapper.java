package com.veterinaria.mapper;

import com.veterinaria.dto.response.EmpresaSummaryResponse;
import com.veterinaria.dto.response.VeterinarioAsociadoResponse;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.EmpresaVeterinario;
import com.veterinaria.model.entity.Veterinario;
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

    public VeterinarioAsociadoResponse toVeterinarioAsociadoResponse(EmpresaVeterinario ev) {
        if (ev == null) return null;

        Veterinario v = ev.getVeterinario();
        String nombreCompleto = v.getNombres() + " " + v.getApepa() + (v.getApema() != null ? " " + v.getApema() : "");

        return new VeterinarioAsociadoResponse(
                v.getIdVeterinario(),
                nombreCompleto,
                v.getEspecialidad(),
                ev.getCargo(),
                ev.getEstadoVinculo()
        );
    }
}
