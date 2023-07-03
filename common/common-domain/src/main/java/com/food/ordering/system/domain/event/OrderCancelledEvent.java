package com.food.ordering.system.domain.event;

import com.food.ordering.system.domain.entity.Order;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher;
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventPublisher = orderCancelledEventPublisher;
    }

    @Override
    public void fire() {
        orderCancelledEventPublisher.publish(this);
    }
}
