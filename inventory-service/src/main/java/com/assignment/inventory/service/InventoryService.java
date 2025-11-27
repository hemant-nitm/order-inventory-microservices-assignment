package com.assignment.inventory.service;

import com.assignment.inventory.dto.InventoryBatchDto;
import com.assignment.inventory.service.factory.InventoryHandlerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryHandlerFactory factory;

    public InventoryService(InventoryHandlerFactory factory) {
        this.factory = factory;
    }

    public List<InventoryBatchDto> getBatches(Long productId) {
        return factory.getHandler("expiry").getBatchesSorted(productId);
    }

    public void updateInventory(Long productId, Integer quantity) {
        factory.getHandler("expiry").updateInventory(productId, quantity);
    }
}
