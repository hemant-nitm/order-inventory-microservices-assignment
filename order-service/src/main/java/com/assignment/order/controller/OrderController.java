package com.assignment.order.controller;

import com.assignment.order.dto.OrderRequest;
import com.assignment.order.dto.OrderResponse;
import com.assignment.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hemant-nitm
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.placeOrder(request);
            // return 201 Created with order details
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException ex) {
            // validation or business rule failure (e.g., insufficient stock)
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            // fallback for unexpected errors
            return ResponseEntity.status(500).body("Failed to place order: " + ex.getMessage());
        }
    }
}
