package com.veterinaria.repository;

import com.veterinaria.model.entity.HorarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioEmpresaRepository extends JpaRepository<HorarioEmpresa, Long> {

    List<HorarioEmpresa> findAllByEmpresaIdEmpresa(Long idEmpresa);
    void deleteByEmpresaIdEmpresa(Long idEmpresa);

}
