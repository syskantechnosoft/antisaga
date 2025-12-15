package com.antisaga.payment.service;

import com.antisaga.common.event.OrderEvent;
import com.antisaga.common.event.PaymentEvent;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.payment.config.PaymentConfig;
import com.antisaga.payment.entity.UserBalance;
import com.antisaga.payment.repository.UserBalanceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentConsumer {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private RabbitTemplate template;

    @RabbitListener(queues = PaymentConfig.ORDER_QUEUE_PAYMENT)
    @Transactional
    public void consumeOrderEvent(OrderEvent orderEvent) {
        if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
             processPayment(orderEvent);
        } else {
             // Handle cancellation if needed
             // e.g., if OrderStatus.ORDER_CANCELLED
        }
    }

    private void processPayment(OrderEvent orderEvent) {
        Integer userId = orderEvent.getOrderRequest().getUserId();
        // Demo Hack: Create balance for new users automatically
        UserBalance userBalance = userBalanceRepository.findById(userId).orElse(new UserBalance(userId, 5000.0));
        
        PaymentStatus paymentStatus = PaymentStatus.PAYMENT_FAILED;
        
        if (userBalance.getPrice() >= orderEvent.getOrderRequest().getTotalAmount()) {
            userBalance.setPrice(userBalance.getPrice() - orderEvent.getOrderRequest().getTotalAmount());
            userBalanceRepository.save(userBalance);
            paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
        }

        PaymentEvent paymentEvent = new PaymentEvent(orderEvent.getOrderId(), orderEvent.getOrderRequest(), paymentStatus);
        
        // Publish Payment Event
        template.convertAndSend(PaymentConfig.PAYMENT_EXCHANGE, PaymentConfig.PAYMENT_ROUTING_KEY, paymentEvent);
    }
}
