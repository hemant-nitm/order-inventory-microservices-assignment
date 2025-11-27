package com.assignment.inventory.service.impl;

import com.assignment.inventory.dto.InventoryBatchDto;
import com.assignment.inventory.entity.InventoryBatch;
import com.assignment.inventory.entity.Product;
import com.assignment.inventory.repository.InventoryBatchRepository;
import com.assignment.inventory.repository.ProductRepository;
import com.assignment.inventory.service.InventoryHandler;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemant-nitm
 */
@Service
public class ExpiryBasedInventoryHandler implements InventoryHandler {

    private final InventoryBatchRepository batchRepo;
    private final ProductRepository productRepo;

    public ExpiryBasedInventoryHandler(InventoryBatchRepository batchRepo,
                                       ProductRepository productRepo) {
        this.batchRepo = batchRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<InventoryBatchDto> getBatchesSorted(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<InventoryBatch> batches =
                batchRepo.findByProductOrderByExpiryDateAsc(product);

        return batches.stream()
                .map(b -> InventoryBatchDto.builder()
                        .id(b.getId())
                        .productId(product.getId())
                        .quantity(b.getQuantity())
                        .expiryDate(b.getExpiryDate())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long productId, int qtyToDeduct) {
        if (qtyToDeduct <= 0) {
            throw new IllegalArgumentException("Quantity to deduct must be > 0");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<InventoryBatch> batches =
                batchRepo.findByProductOrderByExpiryDateAsc(product);

        int remaining = qtyToDeduct;

        for (InventoryBatch batch : batches) {
            if (remaining == 0) break;
            if (batch.getQuantity() == 0) continue;

            int deductFromBatch = Math.min(batch.getQuantity(), remaining);
            batch.setQuantity(batch.getQuantity() - deductFromBatch);
            remaining -= deductFromBatch;

            batchRepo.save(batch);
        }

        if (remaining > 0) {
            throw new IllegalArgumentException(
                    "Insufficient stock for productId " + productId + ". Short by: " + remaining);
        }
    }
}
