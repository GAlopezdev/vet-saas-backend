package com.veterinaria.mapper;

import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.dto.response.HorarioResponse;
import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.entity.HorarioEmpresa;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class HorarioEmpresaMapper {

    public HorarioEmpresa toEntity(HorarioRequest request, Empresa empresa) {
        if (request == null) return null;

        HorarioEmpresa entidad = new HorarioEmpresa();
        entidad.setEmpresa(empresa);
        entidad.setDiaSemana(request.diaSemana());
        entidad.setEstaCerrado(request.estaCerrado());

        if (!request.estaCerrado() && request.horaApertura() != null && request.horaCierre() != null) {
            entidad.setHoraApertura(LocalTime.parse(request.horaApertura()));
            entidad.setHoraCierre(LocalTime.parse(request.horaCierre()));
        }

        return entidad;
    }

    public HorarioResponse toResponse(HorarioEmpresa entidad) {
        if (entidad == null) return null;

        return new HorarioResponse(
                entidad.getIdHorario(),
                entidad.getDiaSemana(),
                entidad.getHoraApertura(),
                entidad.getHoraCierre(),
                entidad.isEstaCerrado()
        );
    }

}
