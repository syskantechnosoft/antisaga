package com.antisaga.order.service;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.dto.OrderItemDTO;
import com.antisaga.order.entity.OrderItem;
import com.antisaga.order.entity.PurchaseOrder;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.event.OrderEvent;
import com.antisaga.order.config.OrderStatusPubConfig;
import com.antisaga.order.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RabbitTemplate template;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDTO orderRequest) {
        PurchaseOrder order = convertToEntity(orderRequest);
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order = orderRepository.save(order);
        
        // Publish Event
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setOrderRequest(orderRequest);
        event.setOrderStatus(OrderStatus.ORDER_CREATED);
        
        template.convertAndSend(OrderStatusPubConfig.ORDER_EXCHANGE, OrderStatusPubConfig.ORDER_ROUTING_KEY, event);
        
        return order;
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }
    
    private PurchaseOrder convertToEntity(OrderRequestDTO dto) {
        PurchaseOrder order = new PurchaseOrder();
        order.setUserId(dto.getUserId());
        order.setTotalAmount(dto.getTotalAmount());
        
        List<OrderItem> items = dto.getItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setProductId(i.getProductId());
            item.setQuantity(i.getQuantity());
            item.setPrice(i.getPrice());
            return item;
        }).collect(Collectors.toList());
        
        order.setItems(items);
        return order;
    }
}
