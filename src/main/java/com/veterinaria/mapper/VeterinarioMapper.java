package com.veterinaria.mapper;

import com.veterinaria.dto.response.VeterinarioSummaryResponse;
import com.veterinaria.model.entity.Veterinario;
import org.springframework.stereotype.Component;

@Component
public class VeterinarioMapper {

    public VeterinarioSummaryResponse toSummaryResponse(Veterinario v) {
        if (v == null) return null;
        return new VeterinarioSummaryResponse(
                v.getIdVeterinario(),
                v.getNombres(),
                v.getApepa(),
                v.getApema(),
                v.getUsuario().getCorreo(),
                v.getEspecialidad(),
                v.getNumeroColegiatura(),
                v.getEstadoRegistro()
        );
    }
}
