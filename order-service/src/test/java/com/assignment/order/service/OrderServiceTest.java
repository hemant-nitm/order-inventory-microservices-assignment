package com.assignment.order.service;

import com.assignment.order.client.InventoryClient;
import com.assignment.order.dto.OrderRequest;
import com.assignment.order.dto.OrderResponse;
import com.assignment.order.entity.Order;
import com.assignment.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private com.assignment.order.service.OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder_success_savesOrder() {
        when(inventoryClient.deductStock(1L, 5)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderRequest req = OrderRequest.builder().productId(1L).quantity(5).build();
        OrderResponse res = orderService.placeOrder(req);

        assertNotNull(res);
        assertEquals(100L, res.getOrderId());
        assertEquals("CREATED", res.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_insufficientStock_throws() {
        when(inventoryClient.deductStock(1L, 50)).thenReturn(false);
        OrderRequest req = OrderRequest.builder().productId(1L).quantity(50).build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(req));
        assertTrue(ex.getMessage().contains("Insufficient stock"));
        verify(orderRepository, never()).save(any());
    }
}