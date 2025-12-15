package com.antisaga.inventory.service;

import com.antisaga.common.event.PaymentEvent;
import com.antisaga.common.event.InventoryEvent;
import com.antisaga.common.dto.OrderItemDTO;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.common.enums.InventoryStatus;
import com.antisaga.inventory.config.InventoryConfig;
import com.antisaga.inventory.entity.Inventory;
import com.antisaga.inventory.repository.InventoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InventoryConsumer {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RabbitTemplate template;

    @RabbitListener(queues = InventoryConfig.PAYMENT_QUEUE_INVENTORY)
    @Transactional
    public void consumePaymentEvent(PaymentEvent paymentEvent) {
        if (PaymentStatus.PAYMENT_COMPLETED.equals(paymentEvent.getPaymentStatus())) {
             processInventory(paymentEvent);
        } else {
             // If payment failed, we do nothing usually or handle cleanup
        }
    }

    private void processInventory(PaymentEvent paymentEvent) {
        List<OrderItemDTO> items = paymentEvent.getOrderRequest().getItems();
        boolean allAvailable = true;
        
        // Check availability
        for (OrderItemDTO item : items) {
             Inventory inventory = inventoryRepository.findById(item.getProductId()).orElse(null);
             if (inventory == null) {
                 // Auto-stock missing products for demo/testing
                 inventory = new Inventory(item.getProductId(), 100);
                 inventoryRepository.save(inventory);
             }
             
             if (inventory.getAvailableAmount() < item.getQuantity()) {
                 allAvailable = false;
                 break;
             }
        }
        
        InventoryStatus status = InventoryStatus.INVENTORY_REJECTED;
        
        if (allAvailable) {
            // Deduct inventory
            for (OrderItemDTO item : items) {
                Inventory inventory = inventoryRepository.findById(item.getProductId()).get();
                inventory.setAvailableAmount(inventory.getAvailableAmount() - item.getQuantity());
                inventoryRepository.save(inventory);
            }
            status = InventoryStatus.INVENTORY_RESERVED;
        }

        InventoryEvent event = new InventoryEvent(paymentEvent.getOrderId(), paymentEvent.getOrderRequest(), status);
        template.convertAndSend(InventoryConfig.INVENTORY_EXCHANGE, InventoryConfig.INVENTORY_ROUTING_KEY, event);
    }
}
