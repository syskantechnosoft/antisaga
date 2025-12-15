package com.antisaga.inventory.controller;

import com.antisaga.inventory.entity.Inventory;
import com.antisaga.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Integer productId) {
        return inventoryRepository.findById(productId)
                .orElse(new Inventory(productId, 0));
    }
}
