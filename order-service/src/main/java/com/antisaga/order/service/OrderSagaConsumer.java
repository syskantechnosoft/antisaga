package com.antisaga.order.service;

import com.antisaga.common.event.PaymentEvent;
import com.antisaga.common.event.InventoryEvent;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.common.enums.InventoryStatus;
import com.antisaga.order.config.OrderStatusPubConfig;
import com.antisaga.order.entity.PurchaseOrder;
import com.antisaga.order.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderSagaConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = OrderStatusPubConfig.PAYMENT_QUEUE_ORDER)
    @Transactional
    public void consumePaymentEvent(PaymentEvent paymentEvent) {
        PurchaseOrder order = orderRepository.findById(paymentEvent.getOrderId()).orElse(null);
        if (order != null) {
            order.setPaymentStatus(paymentEvent.getPaymentStatus());
            if (PaymentStatus.PAYMENT_FAILED.equals(paymentEvent.getPaymentStatus())) {
                order.setOrderStatus(OrderStatus.ORDER_CANCELLED);
            }
            orderRepository.save(order);
        }
    }

    @RabbitListener(queues = OrderStatusPubConfig.INVENTORY_QUEUE_ORDER)
    @Transactional
    public void consumeInventoryEvent(InventoryEvent inventoryEvent) {
        PurchaseOrder order = orderRepository.findById(inventoryEvent.getOrderId()).orElse(null);
        if (order != null) {
            order.setInventoryStatus(inventoryEvent.getInventoryStatus());
            if (InventoryStatus.INVENTORY_RESERVED.equals(inventoryEvent.getInventoryStatus())) {
                order.setOrderStatus(OrderStatus.ORDER_COMPLETED);
            } else {
                order.setOrderStatus(OrderStatus.ORDER_CANCELLED);
            }
            orderRepository.save(order);
        }
    }
}
