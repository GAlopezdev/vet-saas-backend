package com.veterinaria.validation;

import com.veterinaria.dto.request.HorarioRequest;
import com.veterinaria.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmpresaValidator {

    private final EmpresaRepository empresaRepository;

    public boolean existeNombreComercial(String nombreComercial, Long idEmpresaActual) {
        return empresaRepository.existsByNombreComercialAndIdEmpresaNot(nombreComercial, idEmpresaActual);
    }

    public void validarDiasDuplicados(List<HorarioRequest> horarios) {
        long cuentaUnicos = horarios
                .stream()
                .map(HorarioRequest::diaSemana)
                .distinct()
                .count();

        if (cuentaUnicos != horarios.size()) {
            throw new IllegalArgumentException("El horario contiene días de la semana duplicados.");
        }
    }

    public void validarCoherenciaHoras(HorarioRequest h) {
        if (Boolean.FALSE.equals(h.estaCerrado())) {
            if (h.horaApertura() == null || h.horaCierre() == null) {
                throw new IllegalArgumentException("Día " + h.diaSemana() + ": Apertura y cierre son requeridos si el local está abierto.");
            }

            LocalTime apertura = LocalTime.parse(h.horaApertura());
            LocalTime cierre = LocalTime.parse(h.horaCierre());

            if (!apertura.isBefore(cierre)) {
                throw new IllegalArgumentException("Día " + h.diaSemana() + ": La hora de apertura (" + h.horaApertura() +
                        ") debe ser anterior a la de cierre (" + h.horaCierre() + ").");
            }
        }
    }

}
