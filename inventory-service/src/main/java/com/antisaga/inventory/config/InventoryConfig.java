package com.antisaga.inventory.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfig {

    // Consume from Payment
    public static final String PAYMENT_EXCHANGE = "payment-exchange";
    public static final String PAYMENT_QUEUE_INVENTORY = "payment-queue-inventory";
    public static final String PAYMENT_ROUTING_KEY = "payment-routing-key";

    // Publish Inventory
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";
    public static final String INVENTORY_ROUTING_KEY = "inventory-routing-key";

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentQueueInventory() {
        return new Queue(PAYMENT_QUEUE_INVENTORY);
    }

    @Bean
    public Binding bindingPayment(Queue paymentQueueInventory, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueueInventory).to(paymentExchange).with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
