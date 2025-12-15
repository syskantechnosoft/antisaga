package com.antisaga.notification.service;

import com.antisaga.notification.config.NotificationConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @RabbitListener(queues = NotificationConfig.SHIPPING_QUEUE_NOTIFICATION)
    public void consumeShippingEvent(String message) {
        System.out.println("------------------------------------------------");
        System.out.println("NOTIFICATION SERVICE: Email sent!");
        System.out.println("Message: " + message);
        System.out.println("------------------------------------------------");
    }
}
