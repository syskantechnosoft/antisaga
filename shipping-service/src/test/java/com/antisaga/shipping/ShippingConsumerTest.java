package com.antisaga.shipping;

import com.antisaga.common.event.InventoryEvent;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.InventoryStatus;
import com.antisaga.shipping.config.ShippingConfig;
import com.antisaga.shipping.entity.Shipment;
import com.antisaga.shipping.repository.ShipmentRepository;
import com.antisaga.shipping.service.ShippingConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ShippingConsumerTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ShippingConsumer shippingConsumer;

    @Test
    public void testConsumeInventoryEvent_Success() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1);
        InventoryEvent event = new InventoryEvent(UUID.randomUUID(), request, InventoryStatus.INVENTORY_RESERVED);

        shippingConsumer.consumeInventoryEvent(event);

        verify(shipmentRepository).save(any(Shipment.class));
        verify(rabbitTemplate).convertAndSend(eq(ShippingConfig.SHIPPING_EXCHANGE), eq(ShippingConfig.SHIPPING_ROUTING_KEY), any(String.class));
    }
}
