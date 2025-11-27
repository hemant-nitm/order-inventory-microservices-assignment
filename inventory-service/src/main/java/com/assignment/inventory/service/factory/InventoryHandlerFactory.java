package com.assignment.inventory.service.factory;

import com.assignment.inventory.service.InventoryHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hemant-nitm
 */
@Component
public class InventoryHandlerFactory {

    private final InventoryHandler expiryHandler;

    @Autowired
    public InventoryHandlerFactory(InventoryHandler expiryHandler) {
        this.expiryHandler = expiryHandler;
    }

    public InventoryHandler getHandler(String strategy) {
        if ("expiry".equalsIgnoreCase(strategy)) {
            return expiryHandler;
        }
        throw new IllegalArgumentException("Unknown inventory handler strategy: " + strategy);
    }
}
