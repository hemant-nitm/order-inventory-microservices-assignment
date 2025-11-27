package com.assignment.inventory.repository;

import com.assignment.inventory.entity.InventoryBatch;
import com.assignment.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {
    List<InventoryBatch> findByProductOrderByExpiryDateAsc(Product product);
}