package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher;

    public OrderPaidEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {
        super(order, createdAt);
        this.orderPaidEventPublisher = orderPaidEventPublisher;
    }

    @Override
    public void fire() {
        orderPaidEventPublisher.publish(this);
    }
}
