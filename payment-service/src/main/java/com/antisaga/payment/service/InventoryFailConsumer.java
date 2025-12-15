package com.antisaga.payment.service;

import com.antisaga.common.event.InventoryEvent;
import com.antisaga.common.enums.InventoryStatus;
import com.antisaga.payment.config.PaymentConfig;
import com.antisaga.payment.entity.UserBalance;
import com.antisaga.payment.repository.UserBalanceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryFailConsumer {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    // We need to define queue for this binding in Config first.
    // Assuming we bind a queue to inventory-exchange with routing key.
    
    // I will use a fixed queue name here and rely on automatic creation or define in Config.
    // Better to define in config.
    @RabbitListener(queues = "inventory-queue-payment")
    @Transactional
    public void consumeInventoryEvent(InventoryEvent event) {
        if (InventoryStatus.INVENTORY_REJECTED.equals(event.getInventoryStatus())) {
            // Compensate
            UserBalance userBalance = userBalanceRepository.findById(event.getOrderRequest().getUserId()).orElse(null);
            if (userBalance != null) {
                userBalance.setPrice(userBalance.getPrice() + event.getOrderRequest().getTotalAmount());
                userBalanceRepository.save(userBalance);
            }
        }
    }
}
