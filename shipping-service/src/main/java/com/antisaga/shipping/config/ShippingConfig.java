package com.antisaga.shipping.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShippingConfig {

    // Consume Inventory Event
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";
    public static final String INVENTORY_QUEUE_SHIPPING = "inventory-queue-shipping";
    public static final String INVENTORY_ROUTING_KEY = "inventory-routing-key";

    // Publish Shipping Event
    public static final String SHIPPING_EXCHANGE = "shipping-exchange";
    public static final String SHIPPING_ROUTING_KEY = "shipping-routing-key";

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }
    
    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(SHIPPING_EXCHANGE);
    }

    @Bean
    public Queue inventoryQueueShipping() {
        return new Queue(INVENTORY_QUEUE_SHIPPING);
    }

    @Bean
    public Binding bindingInventory(Queue inventoryQueueShipping, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryQueueShipping).to(inventoryExchange).with(INVENTORY_ROUTING_KEY);
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
