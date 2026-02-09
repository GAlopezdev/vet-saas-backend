package com.veterinaria.repository;

import com.veterinaria.model.entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoriaRepository extends JpaRepository<Subcategoria, Long> {
}
