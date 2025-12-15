package com.antisaga.payment;

import com.antisaga.common.event.OrderEvent;
import com.antisaga.common.dto.OrderRequestDTO;
import com.antisaga.common.enums.OrderStatus;
import com.antisaga.common.enums.PaymentStatus;
import com.antisaga.payment.config.PaymentConfig;
import com.antisaga.payment.entity.UserBalance;
import com.antisaga.payment.repository.UserBalanceRepository;
import com.antisaga.payment.service.PaymentConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentConsumerTest {

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentConsumer paymentConsumer;

    @Test
    public void testConsumeOrderEvent_Success() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1);
        request.setTotalAmount(100.0);
        OrderEvent event = new OrderEvent(UUID.randomUUID(), UUID.randomUUID(), request, OrderStatus.ORDER_CREATED);
        event.setOrderId(UUID.randomUUID());

        UserBalance balance = new UserBalance(1, 200.0);
        when(userBalanceRepository.findById(1)).thenReturn(Optional.of(balance));

        paymentConsumer.consumeOrderEvent(event);

        verify(userBalanceRepository, times(1)).save(any(UserBalance.class));
        verify(rabbitTemplate).convertAndSend(eq(PaymentConfig.PAYMENT_EXCHANGE), eq(PaymentConfig.PAYMENT_ROUTING_KEY), any(com.antisaga.common.event.PaymentEvent.class));
    }
}
