package com.veterinaria.repository;

import com.veterinaria.model.entity.PerfilCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilClienteRepository extends JpaRepository<PerfilCliente, Long> {
}
