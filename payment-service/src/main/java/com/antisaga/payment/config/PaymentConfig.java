package com.antisaga.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    
    // Order Exchange
    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_QUEUE_PAYMENT = "order-queue-payment";
    public static final String ORDER_ROUTING_KEY = "order-routing-key";

    // Payment Exchange
    public static final String PAYMENT_EXCHANGE = "payment-exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment-routing-key";
    
    // Inventory Exchange (for compensation)
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";
    public static final String INVENTORY_QUEUE_PAYMENT = "inventory-queue-payment";
    public static final String INVENTORY_ROUTING_KEY = "inventory-routing-key";

    // Exchanges
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }
    
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    // Queues
    @Bean
    public Queue orderQueuePayment() {
        return new Queue(ORDER_QUEUE_PAYMENT);
    }

    @Bean
    public Queue inventoryQueuePayment() {
        return new Queue(INVENTORY_QUEUE_PAYMENT);
    }

    // Bindings
    @Bean
    public Binding bindingOrder(Queue orderQueuePayment, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueuePayment).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding bindingInventory(Queue inventoryQueuePayment, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryQueuePayment).to(inventoryExchange).with(INVENTORY_ROUTING_KEY);
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
