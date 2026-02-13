package com.veterinaria.service.impl;

import com.mercadopago.resources.preference.Preference;
import com.veterinaria.dto.mercadopago.CartItemRequest;
import com.veterinaria.dto.mercadopago.CreateOrderRequest;
import com.veterinaria.dto.mercadopago.PaymentResponse;
import com.veterinaria.model.entity.Order;
import com.veterinaria.model.entity.OrderItem;
import com.veterinaria.model.entity.Producto;
import com.veterinaria.model.enums.OrderStatus;
import com.veterinaria.repository.OrderRepository;
import com.veterinaria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final MercadoPagoService mercadoPagoService;
    private final ProductoRepository productoRepository;

    /**
     * Crea una orden en estado PENDIENTE y genera la preferencia de pago en Mercado Pago.
     * No descuenta stock hasta que el pago sea confirmado.
     */
    @Transactional
    public PaymentResponse createOrderAndPayment(CreateOrderRequest request) {

        validarStockDisponible(request.items());

        BigDecimal total = mercadoPagoService.calculateOrderTotal(request.items());

        Order order = Order.builder()
                .usuarioId(request.usuarioId())
                .total(total)
                .estadoOrden(Order.OrderStatus.PENDIENTE)
                .build();

        request.items().forEach(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .tipoItem(cartItem.tipoItem())
                    .itemId(cartItem.itemId())
                    .cantidad(cartItem.cantidad())
                    .precioUnitario(cartItem.precioUnitario())
                    .build();

            order.addItem(orderItem);
        });

        Order savedOrder = orderRepository.save(order);

        Preference preference = mercadoPagoService.createPreference(savedOrder, request.items());

        savedOrder.setPreferenceId(preference.getId());
        orderRepository.save(savedOrder);

        LOGGER.info("Order created id={} userId={} total={}",
                savedOrder.getIdOrden(),
                request.usuarioId(),
                total);

        return new PaymentResponse(
                savedOrder.getIdOrden(),
                preference.getId(),
                preference.getInitPoint(),
                total,
                savedOrder.getEstadoOrden().name()
        );
    }

    /**
     * Actualiza el estado de la orden segun el resultado recibido desde Mercado Pago.
     * Si el pago es aprobado, se descuenta stock.
     */
    @Transactional
    public void updatePaymentStatus(Long orderId, String paymentId, String mpStatus, String merchantOrderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found: " + orderId));

        order.setPaymentIdMp(paymentId);
        order.setPaymentStatus(mpStatus);
        order.setMerchantOrderId(merchantOrderId);

        Order.OrderStatus nuevoEstado = Order.mapFromMercadoPagoStatus(mpStatus);
        order.setEstadoOrden(nuevoEstado);

        if (nuevoEstado == Order.OrderStatus.PAGADO) {
            order.setFechaPago(LocalDateTime.now());
            descontarStockDeOrden(order);
            LOGGER.info("Payment approved orderId={}", orderId);
        } else if (nuevoEstado == Order.OrderStatus.ERROR_PROCESAMIENTO) {
            LOGGER.error("Payment processing error orderId={} mpStatus={}", orderId, mpStatus);
        }

        orderRepository.save(order);

        LOGGER.info("Order status updated orderId={} status={}", orderId, nuevoEstado);
    }

    @Transactional(readOnly = true)
    public Order findByPreferenceId(String preferenceId) {
        return orderRepository.findByPreferenceId(preferenceId)
                .orElseThrow(() -> new IllegalStateException("Order not found for preferenceId"));
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByUser(Long usuarioId) {
        return orderRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found: " + orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> findByEstado(OrderStatus estado) {
        return orderRepository.findByEstadoOrden(estado);
    }

    // Verifica que exista stock suficiente antes de crear la orden.
    private void validarStockDisponible(List<CartItemRequest> items) {

        for (CartItemRequest item : items) {

            Producto producto = productoRepository.findById(item.itemId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Product not found: " + item.itemId()));

            if (producto.getStock() < item.cantidad()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + producto.getIdProducto());
            }
        }
    }

    // Descuenta stock solo cuando el pago ha sido confirmado.
    private void descontarStockDeOrden(Order order) {

        for (OrderItem item : order.getItems()) {

            Producto producto = productoRepository.findById(item.getItemId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Critical error: product missing during stock deduction"));

            int nuevoStock = producto.getStock() - item.getCantidad();

            if (nuevoStock < 0) {
                throw new IllegalStateException(
                        "Stock inconsistency detected for product: " + producto.getIdProducto());
            }

            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }

        LOGGER.info("Stock updated for orderId={}", order.getIdOrden());
    }
}
