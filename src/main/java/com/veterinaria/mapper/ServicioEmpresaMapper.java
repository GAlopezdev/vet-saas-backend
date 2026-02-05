package com.veterinaria.mapper;

import com.veterinaria.dto.response.ServicioResponse;
import com.veterinaria.model.entity.ServicioEmpresa;
import org.springframework.stereotype.Component;

@Component
public class ServicioEmpresaMapper {

    public ServicioResponse toResponse(ServicioEmpresa servicio) {
        return new ServicioResponse(
                servicio.getIdServicio(),
                servicio.getNombreServicio(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getActivo(),
                servicio.getCreatedAt()
        );
    }
}
