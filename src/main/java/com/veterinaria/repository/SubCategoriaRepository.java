package com.veterinaria.repository;

import com.veterinaria.model.entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface SubCategoriaRepository extends JpaRepository<Subcategoria, Long> {
    List<Subcategoria> findByCategoria_IdCategoria(Long categoriaId);
}
