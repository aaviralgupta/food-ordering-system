package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.entity.Order;
import com.food.ordering.system.domain.entity.Restaurant;
import com.food.ordering.system.domain.event.OrderCancelledEvent;
import com.food.ordering.system.domain.event.OrderCreatedEvent;
import com.food.ordering.system.domain.event.OrderPaidEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher);
    OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher);

    void cancelOrder(Order order, List<String> failureMessages);
}
