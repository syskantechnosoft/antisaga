package com.antisaga.common.event;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.OrderStatus;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private UUID eventId = UUID.randomUUID();
    private UUID orderId; // Internal order tracking ID
    private OrderRequestDTO orderRequest;
    private OrderStatus orderStatus;

    public OrderEvent(OrderRequestDTO orderRequest, OrderStatus orderStatus) {
        this.orderRequest = orderRequest;
        this.orderStatus = orderStatus;
    }
}
