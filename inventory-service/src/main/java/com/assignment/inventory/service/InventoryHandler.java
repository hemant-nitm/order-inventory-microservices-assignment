package com.assignment.inventory.service;

import com.assignment.inventory.dto.InventoryBatchDto;

import java.util.List;

/**
 * @author hemant-nitm
 */
public interface InventoryHandler {

    // Returns inventory batches sorted by expiry date
    List<InventoryBatchDto> getBatchesSorted(Long productId);

    // Deduct inventory based on earliest expiry (FEFO)
    void updateInventory(Long productId, int qtyToDeduct) throws IllegalArgumentException;
}
