package com.antisaga.common.event;

import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.PaymentStatus;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private UUID eventId = UUID.randomUUID();
    private UUID orderId;
    private OrderRequestDTO orderRequest;
    private PaymentStatus paymentStatus;

    public PaymentEvent(UUID orderId, OrderRequestDTO orderRequest, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.orderRequest = orderRequest;
        this.paymentStatus = paymentStatus;
    }
}
