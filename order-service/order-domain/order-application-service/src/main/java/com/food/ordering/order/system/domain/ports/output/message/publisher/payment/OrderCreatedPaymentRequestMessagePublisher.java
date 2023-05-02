package com.food.ordering.order.system.domain.ports.output.message.publisher.payment;

import com.food.ordering.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {


}
