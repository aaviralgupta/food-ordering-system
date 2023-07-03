package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.valueobject.ProductId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.food.ordering.system.order.service.domain.DomainConstants.UTC;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{


    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                      Restaurant restaurant,
                                                      DomainEventPublisher<OrderCreatedEvent>
                                                                  orderCreatedEventPublisher) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id : {} is initialized", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventPublisher);
    }

    @Override
    public OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {
        order.pay();
        log.info("Order with id: {} is paid",order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventPublisher);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved",order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,
                                                  DomainEventPublisher<OrderCancelledEvent>
                                                          orderCancelledEventPublisher) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling with id: {}",order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCancelledEventPublisher);
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled ",order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()){
            throw new OrderDomainException("Restraunt with Id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<ProductId, Product> restaurantProductsMap = restaurant.getProducts().stream().
                collect(Collectors.toMap(Product::getId, Function.identity()));
        order.getOrderItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurantProductsMap.get(currentProduct.getId());
            currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                    restaurantProduct.getPrice());
        });
    }
}
