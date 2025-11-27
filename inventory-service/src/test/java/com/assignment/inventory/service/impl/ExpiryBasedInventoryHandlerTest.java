package com.assignment.inventory.service.impl;

import com.assignment.inventory.entity.InventoryBatch;
import com.assignment.inventory.entity.Product;
import com.assignment.inventory.repository.InventoryBatchRepository;
import com.assignment.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpiryBasedInventoryHandlerTest {

    @Mock
    private InventoryBatchRepository batchRepo;

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ExpiryBasedInventoryHandler handler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBatchesSorted_returnsDtosInOrder() {
        Product p = Product.builder().id(1L).name("X").build();
        when(productRepo.findById(1L)).thenReturn(Optional.of(p));

        InventoryBatch b1 = InventoryBatch.builder().id(1L).product(p).quantity(10).expiryDate(LocalDate.now().plusDays(5)).build();
        InventoryBatch b2 = InventoryBatch.builder().id(2L).product(p).quantity(20).expiryDate(LocalDate.now().plusDays(10)).build();

        when(batchRepo.findByProductOrderByExpiryDateAsc(p)).thenReturn(List.of(b1, b2));

        var dtos = handler.getBatchesSorted(1L);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
    }

    @Test
    void updateInventory_deductsAcrossBatches() {
        Product p = Product.builder().id(1L).name("X").build();
        when(productRepo.findById(1L)).thenReturn(Optional.of(p));

        InventoryBatch b1 = InventoryBatch.builder().id(1L).product(p).quantity(5).expiryDate(LocalDate.now().plusDays(1)).build();
        InventoryBatch b2 = InventoryBatch.builder().id(2L).product(p).quantity(10).expiryDate(LocalDate.now().plusDays(10)).build();

        when(batchRepo.findByProductOrderByExpiryDateAsc(p)).thenReturn(List.of(b1, b2));
        when(batchRepo.save(any(InventoryBatch.class))).thenAnswer(inv -> inv.getArgument(0));

        handler.updateInventory(1L, 12);

        // b1 becomes 0, b2 becomes 3
        assertEquals(0, b1.getQuantity());
        assertEquals(3, b2.getQuantity());
        verify(batchRepo, atLeast(1)).save(b1);
        verify(batchRepo, atLeast(1)).save(b2);
    }

    @Test
    void updateInventory_throwsWhenInsufficient() {
        Product p = Product.builder().id(1L).name("X").build();
        when(productRepo.findById(1L)).thenReturn(Optional.of(p));

        InventoryBatch b1 = InventoryBatch.builder().id(1L).product(p).quantity(2).expiryDate(LocalDate.now().plusDays(1)).build();
        when(batchRepo.findByProductOrderByExpiryDateAsc(p)).thenReturn(List.of(b1));

        RuntimeException ex = assertThrows(IllegalArgumentException.class, () -> handler.updateInventory(1L, 5));
        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }
}
