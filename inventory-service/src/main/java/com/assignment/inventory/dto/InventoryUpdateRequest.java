package com.assignment.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryUpdateRequest {
    private Long productId;
    private Integer quantity;   // Quantity to deduct from inventory
}
