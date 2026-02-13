package com.veterinaria.repository;

import com.veterinaria.model.entity.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO'")
    Page<Producto> findAllActivos(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.usuario.correo = :correo AND p.estado = 'ACTIVO'")
    Page<Producto> findByUsuarioCorreoActivos(String correo, Pageable pageable);

    Page<Producto> findByEstado(String estado, Pageable pageable);

    Page<Producto> findByUsuario_IdUsuario(Long usuarioId, Pageable pageable);

    Page<Producto> findByNombreContainingIgnoreCaseAndEstado(String nombre, String estado, Pageable pageable);

    /**
     * Busca producto con bloqueo pesimista para evitar race conditions en stock.
     * Se usa en transacciones de compra para garantizar consistencia.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.idProducto = :id")
    Optional<Producto> findByIdWithLock(@Param("id") Long id);

    /**
     * Busca producto sin bloqueo (para consultas de lectura)
     */
    Optional<Producto> findByIdProducto(Long id);
}
