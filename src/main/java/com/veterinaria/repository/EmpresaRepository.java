package com.veterinaria.repository;

import com.veterinaria.model.entity.Empresa;
import com.veterinaria.model.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByUsuarioCorreo(String correo);

    boolean existsByRuc(String ruc);

    List<Empresa> findByEstadoRegistro(EstadoRegistro estado);

}
