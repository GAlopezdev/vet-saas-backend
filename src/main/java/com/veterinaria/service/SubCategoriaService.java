package com.veterinaria.service;

import com.veterinaria.dto.request.SubcategoriaDTO;

import java.util.List;

public interface SubCategoriaService {

    SubcategoriaDTO crearSubcategoria(SubcategoriaDTO dto);
    SubcategoriaDTO obtenerPorId(Long id);
    List<SubcategoriaDTO> listarTodas();
    List<SubcategoriaDTO> listarPorCategoria(Long categoriaId);
    SubcategoriaDTO actualizar(Long id, SubcategoriaDTO dto);
    void eliminar(Long id);

}
