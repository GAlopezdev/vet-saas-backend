package com.veterinaria.repository;

import com.veterinaria.model.entity.TipoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresa, Long> {
}
