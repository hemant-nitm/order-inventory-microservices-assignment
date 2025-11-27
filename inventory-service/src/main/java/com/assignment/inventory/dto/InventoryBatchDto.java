package com.assignment.inventory.dto;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryBatchDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private LocalDate expiryDate;
}

