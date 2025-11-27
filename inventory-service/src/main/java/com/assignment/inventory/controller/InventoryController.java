package com.assignment.inventory.controller;


import com.assignment.inventory.dto.InventoryBatchDto;
import com.assignment.inventory.dto.InventoryUpdateRequest;
import com.assignment.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hemant-nitm
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // GET /inventory/{productId} - sorting done on expiry date
    @GetMapping("/{productId}")
    public ResponseEntity<List<InventoryBatchDto>> getBatches(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getBatches(productId));
    }

    // POST /inventory/update - deduct stock from inventory after order
    @PostMapping("/update")
    public ResponseEntity<?> updateInventory(@RequestBody InventoryUpdateRequest request) {
        try {
            service.updateInventory(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
