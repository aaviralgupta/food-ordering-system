package com.food.ordering.order.system.domain.ports.input.message.listener.payment;

import com.food.ordering.order.system.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
