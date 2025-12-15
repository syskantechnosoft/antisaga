package com.antisaga.shipping.service;

import com.antisaga.common.event.InventoryEvent;
import com.antisaga.common.enums.InventoryStatus;
import com.antisaga.shipping.config.ShippingConfig;
import com.antisaga.shipping.entity.Shipment;
import com.antisaga.shipping.repository.ShipmentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ShippingConsumer {

    @Autowired
    private ShipmentRepository shipmentRepository;
    
    @Autowired
    private RabbitTemplate template;

    @RabbitListener(queues = ShippingConfig.INVENTORY_QUEUE_SHIPPING)
    public void consumeInventoryEvent(InventoryEvent event) {
        if (InventoryStatus.INVENTORY_RESERVED.equals(event.getInventoryStatus())) {
            Shipment shipment = new Shipment();
            shipment.setOrderId(event.getOrderId());
            shipment.setUserId(event.getOrderRequest().getUserId());
            shipment.setStatus("SHIPPED");
            shipmentRepository.save(shipment);
            
            // Publish Shipping Event (String for simplicity or custom Event)
            // Just sending orderId for now
            template.convertAndSend(ShippingConfig.SHIPPING_EXCHANGE, ShippingConfig.SHIPPING_ROUTING_KEY, "Shipping started for Order: " + event.getOrderId());
        }
    }
}
