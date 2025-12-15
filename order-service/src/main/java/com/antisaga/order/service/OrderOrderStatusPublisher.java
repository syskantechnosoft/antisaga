package com.antisaga.order.service;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.event.OrderEvent;
import com.antisaga.order.config.OrderStatusPubConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrderOrderStatusPublisher {

    @Autowired
    private RabbitTemplate template;

    public void publishOrderEvent(OrderRequestDTO orderRequest, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequest, orderStatus);
        // We'll separate logic: Order Created -> Payment Service.
        // We might want to persist the Order ID in the event.
        // For now, assume this is just fire and forget to the exchange.
        template.convertAndSend(OrderStatusPubConfig.ORDER_EXCHANGE, OrderStatusPubConfig.ORDER_ROUTING_KEY, orderEvent);
    }
}
