package com.assignment.inventory.service;

import com.assignment.inventory.dto.InventoryBatchDto;
import com.assignment.inventory.service.factory.InventoryHandlerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @Mock
    private InventoryHandlerFactory factory;

    @Mock
    private InventoryHandler handler;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBatches_delegatesToFactoryHandler() {
        when(factory.getHandler("expiry")).thenReturn(handler);
        when(handler.getBatchesSorted(1L)).thenReturn(List.of(
                InventoryBatchDto.builder().id(10L).productId(1L).quantity(50).expiryDate(null).build()
        ));

        List<InventoryBatchDto> result = inventoryService.getBatches(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(factory).getHandler("expiry");
        verify(handler).getBatchesSorted(1L);
    }

    @Test
    void updateInventory_delegatesToFactoryHandler() {
        when(factory.getHandler("expiry")).thenReturn(handler);
        doNothing().when(handler).updateInventory(1L, 5);

        inventoryService.updateInventory(1L, 5);

        verify(factory).getHandler("expiry");
        verify(handler).updateInventory(1L, 5);
    }
}
