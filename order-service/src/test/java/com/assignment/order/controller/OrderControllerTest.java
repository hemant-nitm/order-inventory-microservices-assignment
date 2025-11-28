package com.assignment.order.controller;

import com.assignment.order.client.InventoryClient;
import com.assignment.order.dto.OrderRequest;
import com.assignment.order.dto.OrderResponse;
import com.assignment.order.entity.Order;
import com.assignment.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private InventoryClient inventoryClient;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/order";
        orderRepository.deleteAll();
        // default behavior can be set per-test
        reset(inventoryClient);
    }

    @Test
    void placeOrder_whenInventoryAvailable_returns201_andPersistsOrder() {
        when(inventoryClient.deductStock(1L, 5)).thenReturn(true);

        OrderRequest req = OrderRequest.builder()
                .productId(1L)
                .quantity(5)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<OrderResponse> response = restTemplate.postForEntity(baseUrl, entity, OrderResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getOrderId()).isNotNull();
        assertThat(body.getProductId()).isEqualTo(1L);
        assertThat(body.getQuantity()).isEqualTo(5);
        assertThat(body.getStatus()).isEqualTo("CREATED");
        assertThat(body.getCreatedAt()).isNotNull();

        Optional<Order> saved = orderRepository.findById(body.getOrderId());
        assertThat(saved).isPresent();
        assertThat(saved.get().getProductId()).isEqualTo(1L);
        assertThat(saved.get().getQuantity()).isEqualTo(5);

        verify(inventoryClient, times(1)).deductStock(1L, 5);
    }

    @Test
    void placeOrder_whenInventoryUnavailable_returns400_andDoesNotPersist() {
        when(inventoryClient.deductStock(2L, 50)).thenReturn(false);

        OrderRequest req = OrderRequest.builder()
                .productId(2L)
                .quantity(50)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsIgnoringCase("insufficient");

        assertThat(orderRepository.findAll()).isEmpty();

        verify(inventoryClient, times(1)).deductStock(2L, 50);
    }
}
