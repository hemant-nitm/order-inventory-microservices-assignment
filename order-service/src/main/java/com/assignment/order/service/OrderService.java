package com.assignment.order.service;

import com.assignment.order.client.InventoryClient;
import com.assignment.order.dto.OrderRequest;
import com.assignment.order.dto.OrderResponse;
import com.assignment.order.entity.Order;
import com.assignment.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author hemant-nitm
 */
@Service
public class OrderService {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public OrderService(InventoryClient inventoryClient, OrderRepository orderRepository) {
        this.inventoryClient = inventoryClient;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getProductId() == null || request.getProductId() <= 0) {
            throw new IllegalArgumentException("Invalid productId");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        // Try to deduct stock from Inventory Service
        boolean updated = inventoryClient.deductStock(request.getProductId(), request.getQuantity());
        if (!updated) {
            // throw error
            throw new IllegalArgumentException("Insufficient stock or inventory service unavailable");
        }

        // Persist the order with CREATED status
        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        Order saved = orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(saved.getId())
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
