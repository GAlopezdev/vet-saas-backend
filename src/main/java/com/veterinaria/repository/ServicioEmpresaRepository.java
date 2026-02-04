package com.veterinaria.repository;

import com.veterinaria.model.entity.ServicioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioEmpresaRepository extends JpaRepository<ServicioEmpresa, Long> {

    List<ServicioEmpresa> findByEmpresaIdEmpresa(Long idEmpresa);

}
