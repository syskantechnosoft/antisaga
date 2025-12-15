package com.antisaga.inventory;

import com.antisaga.common.event.PaymentEvent;
import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.dto.OrderItemDTO;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.inventory.config.InventoryConfig;
import com.antisaga.inventory.entity.Inventory;
import com.antisaga.inventory.repository.InventoryRepository;
import com.antisaga.inventory.service.InventoryConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryConsumerTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private InventoryConsumer inventoryConsumer;

    @Test
    public void testConsumePaymentEvent_Success() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1);
        OrderItemDTO item = new OrderItemDTO(1, 2, 50.0);
        request.setItems(Collections.singletonList(item));
        
        PaymentEvent event = new PaymentEvent(UUID.randomUUID(), request, PaymentStatus.PAYMENT_COMPLETED);

        Inventory inv = new Inventory(1, 10);
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inv));

        inventoryConsumer.consumePaymentEvent(event);

        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        verify(rabbitTemplate).convertAndSend(eq(InventoryConfig.INVENTORY_EXCHANGE), eq(InventoryConfig.INVENTORY_ROUTING_KEY), any(com.antisaga.common.event.InventoryEvent.class));
    }
}
