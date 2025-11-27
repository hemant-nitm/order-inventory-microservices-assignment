package com.assignment.order.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String status; // CREATED, FAILED, REJECTED
    private LocalDateTime createdAt;
}
