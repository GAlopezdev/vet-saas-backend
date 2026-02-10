package com.veterinaria.service.impl;

import com.veterinaria.dto.request.SubcategoriaDTO;
import com.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.mapper.SubcategoriaMapper;
import com.veterinaria.model.entity.Categoria;
import com.veterinaria.model.entity.Subcategoria;
import com.veterinaria.repository.CategoriaRepository;
import com.veterinaria.repository.SubCategoriaRepository;
import com.veterinaria.service.SubCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubcategoriaServiceImpl implements SubCategoriaService {

    private final SubCategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaMapper subcategoriaMapper;

    @Override
    @Transactional
    public SubcategoriaDTO crearSubcategoria(SubcategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));

        Subcategoria subcategoria = subcategoriaMapper.toEntity(dto, categoria);
        return subcategoriaMapper.toDto(subcategoriaRepository.save(subcategoria));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoriaDTO> listarTodas() {
        return subcategoriaRepository.findAll().stream()
                .map(subcategoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoriaDTO> listarPorCategoria(Long categoriaId) {
        return subcategoriaRepository.findByCategoria_IdCategoria(categoriaId).stream()
                .map(subcategoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubcategoriaDTO obtenerPorId(Long id) {
        return subcategoriaRepository.findById(id)
                .map(subcategoriaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));
    }

    @Override
    @Transactional
    public SubcategoriaDTO actualizar(Long id, SubcategoriaDTO dto) {
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        subcategoria.setNombre(dto.nombre());
        // Si quieres permitir cambiar la categoría padre:
        if (!subcategoria.getCategoria().getIdCategoria().equals(dto.categoriaId())) {
            Categoria nuevaCategoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nueva categoría padre no encontrada"));
            subcategoria.setCategoria(nuevaCategoria);
        }

        return subcategoriaMapper.toDto(subcategoriaRepository.save(subcategoria));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!subcategoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subcategoría no encontrada");
        }
        subcategoriaRepository.deleteById(id);
    }
}