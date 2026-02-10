package com.veterinaria.service;

import com.veterinaria.dto.request.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    List<CategoriaDTO> listarTodas();

    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);

    CategoriaDTO obtenerCategoriaPorId(Long idCategoria);

    CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoriaDTO);

    void eliminarCategoria(Long idCategoria);

}
