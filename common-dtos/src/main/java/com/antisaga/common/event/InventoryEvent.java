package com.antisaga.common.event;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.InventoryStatus;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private UUID eventId = UUID.randomUUID();
    private UUID orderId;
    private OrderRequestDTO orderRequest;
    private InventoryStatus inventoryStatus;

    public InventoryEvent(UUID orderId, OrderRequestDTO orderRequest, InventoryStatus inventoryStatus) {
        this.orderId = orderId;
        this.orderRequest = orderRequest;
        this.inventoryStatus = inventoryStatus;
    }
}
