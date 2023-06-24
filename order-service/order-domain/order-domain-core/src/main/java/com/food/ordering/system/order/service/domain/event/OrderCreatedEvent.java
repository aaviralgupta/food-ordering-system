package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher;
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        super(order, createdAt);
        this.orderCreatedEventPublisher = orderCreatedEventPublisher;
    }

    @Override
    public void fire() {
        orderCreatedEventPublisher.publish(this);
    }
}
