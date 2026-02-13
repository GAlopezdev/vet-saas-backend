package com.veterinaria.repository;

import com.veterinaria.model.entity.Order;
import com.veterinaria.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByPreferenceId(String preferenceId);

    Optional<Order> findByPaymentIdMp(String paymentIdMp);

    List<Order> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    List<Order> findByEstadoOrden(OrderStatus estadoOrden);
}