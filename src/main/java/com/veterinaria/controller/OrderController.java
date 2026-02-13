package com.veterinaria.controller;

import com.veterinaria.dto.mercadopago.CreateOrderRequest;
import com.veterinaria.dto.mercadopago.PaymentResponse;
import com.veterinaria.dto.request.UpdateEstadoOrdenRequest;
import com.veterinaria.dto.response.OrdenSeguimientoResponse;
import com.veterinaria.model.entity.Order;
import com.veterinaria.service.impl.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("🛒 Solicitud de creación de orden recibida");
        PaymentResponse response = orderService.createOrderAndPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long usuarioId) {
        List<Order> orders = orderService.findOrdersByUser(usuarioId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        // Implementar según necesidad
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mis-pedidos/{usuarioId}")
    public ResponseEntity<List<OrdenSeguimientoResponse>> obtenerMisPedidos(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) Order.OrderStatus estado) {

        return ResponseEntity.ok(orderService.obtenerMisPedidos(usuarioId, estado));
    }

    // GET /api/ordenes/seguimiento/456
    @GetMapping("/seguimiento/{idOrden}")
    public ResponseEntity<OrdenSeguimientoResponse> obtenerSeguimiento(@PathVariable Long idOrden) {
        return ResponseEntity.ok(orderService.obtenerDetalleSeguimiento(idOrden));
    }

    // PATCH /api/ordenes/456/estado
    @PatchMapping("/{idOrden}/estado")
    public ResponseEntity<OrdenSeguimientoResponse> actualizarEstado(
            @PathVariable Long idOrden,
            @Valid @RequestBody UpdateEstadoOrdenRequest request) {

        return ResponseEntity.ok(orderService.actualizarEstado(idOrden, request));
    }
}