package com.veterinaria.mapper;

import com.veterinaria.dto.request.CategoriaDTO;
import com.veterinaria.dto.request.SubcategoriaDTO;
import com.veterinaria.model.entity.Categoria;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoriaMapper {

    private final SubcategoriaMapper subcategoriaMapper;

    public CategoriaDTO toDto(Categoria entity) {
        if (entity == null) return null;

        return new CategoriaDTO(
                entity.getIdCategoria(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }

    public List<SubcategoriaDTO> toSubcategoriaDtoList(Categoria entity) {
        if (entity.getSubcategorias() == null) return List.of();
        return entity.getSubcategorias().stream()
                .map(subcategoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    public Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) return null;

        return Categoria.builder()
                .idCategoria(dto.idCategoria())
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
    }
}