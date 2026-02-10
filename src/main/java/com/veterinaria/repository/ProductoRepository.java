package com.veterinaria.repository;

import com.veterinaria.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO'")
    Page<Producto> findAllActivos(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.usuario.correo = :correo AND p.estado = 'ACTIVO'")
    Page<Producto> findByUsuarioCorreoActivos(String correo, Pageable pageable);

    Page<Producto> findByEstado(String estado, Pageable pageable);

    Page<Producto> findByUsuario_IdUsuario(Long usuarioId, Pageable pageable);

    Page<Producto> findByNombreContainingIgnoreCaseAndEstado(String nombre, String estado, Pageable pageable);

}
