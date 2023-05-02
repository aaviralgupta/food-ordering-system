package com.food.ordering.order.system.domain.ports.output.message.publisher.restaurantapproval;

import com.food.ordering.order.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
