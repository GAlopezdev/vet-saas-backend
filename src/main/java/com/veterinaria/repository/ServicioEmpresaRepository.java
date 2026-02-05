package com.veterinaria.repository;

import com.veterinaria.model.entity.ServicioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioEmpresaRepository extends JpaRepository<ServicioEmpresa, Long> {

    @Query("SELECT s FROM ServicioEmpresa s WHERE s.empresa.idEmpresa = :idEmpresa")
    List<ServicioEmpresa> findAllByEmpresaId(@Param("idEmpresa") Long idEmpresa);

    Optional<ServicioEmpresa> findByIdServicioAndEmpresaIdEmpresa(Long idServicio, Long idEmpresa);
}
