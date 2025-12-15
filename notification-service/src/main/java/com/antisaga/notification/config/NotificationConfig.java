package com.antisaga.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    public static final String SHIPPING_EXCHANGE = "shipping-exchange";
    public static final String SHIPPING_QUEUE_NOTIFICATION = "shipping-queue-notification";
    public static final String SHIPPING_ROUTING_KEY = "shipping-routing-key";

    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(SHIPPING_EXCHANGE);
    }

    @Bean
    public Queue shippingQueueNotification() {
        return new Queue(SHIPPING_QUEUE_NOTIFICATION);
    }

    @Bean
    public Binding bindingShipping(Queue shippingQueueNotification, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shippingQueueNotification).to(shippingExchange).with(SHIPPING_ROUTING_KEY);
    }
}
