package com.veterinaria.mapper;

import com.veterinaria.dto.request.SubcategoriaDTO;
import com.veterinaria.model.entity.Categoria;
import com.veterinaria.model.entity.Subcategoria;
import org.springframework.stereotype.Component;

@Component
public class SubcategoriaMapper {

    public SubcategoriaDTO toDto(Subcategoria entity) {
        if (entity == null) return null;

        return new SubcategoriaDTO(
                entity.getIdSubcategoria(),
                entity.getNombre(),
                entity.getCategoria() != null ? entity.getCategoria().getIdCategoria() : null,
                entity.getCategoria() != null ? entity.getCategoria().getNombre() : null
        );
    }

    public Subcategoria toEntity(SubcategoriaDTO dto, Categoria categoriaPadre) {
        if (dto == null) return null;

        return Subcategoria.builder()
                .idSubcategoria(dto.idSubcategoria())
                .nombre(dto.nombre())
                .categoria(categoriaPadre)
                .build();
    }
}