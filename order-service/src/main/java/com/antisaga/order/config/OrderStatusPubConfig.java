package com.antisaga.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderStatusPubConfig {

    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_ROUTING_KEY = "order-routing-key";

    // Listen to Payment and Inventory
    public static final String PAYMENT_EXCHANGE = "payment-exchange";
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";
    
    public static final String PAYMENT_QUEUE_ORDER = "payment-queue-order";
    public static final String INVENTORY_QUEUE_ORDER = "inventory-queue-order";
    
    public static final String PAYMENT_ROUTING_KEY = "payment-routing-key";
    public static final String INVENTORY_ROUTING_KEY = "inventory-routing-key";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }
    
    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }
    
    @Bean
    public Queue paymentQueueOrder() {
        return new Queue(PAYMENT_QUEUE_ORDER);
    }
    
    @Bean
    public Queue inventoryQueueOrder() {
        return new Queue(INVENTORY_QUEUE_ORDER);
    }
    
    @Bean
    public Binding bindingPayment(Queue paymentQueueOrder, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueueOrder).to(paymentExchange).with(PAYMENT_ROUTING_KEY);
    }
    
    @Bean
    public Binding bindingInventory(Queue inventoryQueueOrder, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryQueueOrder).to(inventoryExchange).with(INVENTORY_ROUTING_KEY);
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
