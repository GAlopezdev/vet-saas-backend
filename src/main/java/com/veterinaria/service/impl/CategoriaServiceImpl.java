package com.veterinaria.service.impl;

import com.veterinaria.dto.request.CategoriaDTO;
import com.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.mapper.CategoriaMapper;
import com.veterinaria.model.entity.Categoria;
import com.veterinaria.repository.CategoriaRepository;
import com.veterinaria.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return categoriaMapper.toDto(categoria);
    }

    @Override
    @Transactional
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        categoria.setNombre(dto.nombre());
        categoria.setDescripcion(dto.descripcion());

        return categoriaMapper.toDto(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}