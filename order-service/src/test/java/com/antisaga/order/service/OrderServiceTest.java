package com.antisaga.order.service;

import com.antisaga.common.dto.OrderItemDTO;
import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.event.OrderEvent;
import com.antisaga.order.config.OrderStatusPubConfig;
import com.antisaga.order.entity.PurchaseOrder;
import com.antisaga.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        // Arrange
        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1);
        request.setTotalAmount(100.0);
        OrderItemDTO item = new OrderItemDTO(1, 1, 100.0);
        request.setItems(Collections.singletonList(item));

        PurchaseOrder savedOrder = new PurchaseOrder();
        savedOrder.setId(UUID.randomUUID());
        savedOrder.setUserId(1);
        savedOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        savedOrder.setTotalAmount(100.0);

        when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(savedOrder);

        // Act
        PurchaseOrder result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.ORDER_CREATED, result.getOrderStatus());
        assertEquals(1, result.getUserId());
        
        verify(orderRepository).save(any(PurchaseOrder.class));
        verify(rabbitTemplate).convertAndSend(eq(OrderStatusPubConfig.ORDER_EXCHANGE), eq(OrderStatusPubConfig.ORDER_ROUTING_KEY), any(OrderEvent.class));
    }
}
