package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.order.service.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
