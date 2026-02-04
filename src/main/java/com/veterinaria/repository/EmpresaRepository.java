package com.veterinaria.repository;

import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByUsuarioCorreo(String correo);

    boolean existsByRuc(String ruc);

    @EntityGraph(attributePaths = {"usuario"})
    Page<Empresa> findByEstadoRegistro(EstadoRegistro estado, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario"})
    @Query("SELECT e FROM Empresa e")
    Page<Empresa> findAllEmpresas(Pageable pageable);

    boolean existsByNombreComercialAndIdEmpresaNot(String nombre, Long id);
}
