package com.veterinaria.service.impl;

import com.veterinaria.dto.response.TipoEmpresaResponse;
import com.veterinaria.mapper.TipoEmpresaMapper;
import com.veterinaria.repository.TipoEmpresaRepository;
import com.veterinaria.service.TipoEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEmpresaServiceImpl implements TipoEmpresaService {

    private final TipoEmpresaRepository tipoEmpresaRepository;
    private final TipoEmpresaMapper tipoEmpresaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TipoEmpresaResponse> listarTipos() {
        return tipoEmpresaRepository.findAll()
                .stream()
                .map(tipoEmpresaMapper::toResponse)
                .toList();
    }
}
