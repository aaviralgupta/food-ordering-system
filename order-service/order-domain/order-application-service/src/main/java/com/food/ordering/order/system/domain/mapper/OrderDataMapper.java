package com.food.ordering.order.system.domain.mapper;

import com.food.ordering.order.system.domain.dto.create.CreateOrderCommand;
import com.food.ordering.order.system.domain.dto.create.CreateOrderResponse;
import com.food.ordering.order.system.domain.dto.create.OrderAddress;
import com.food.ordering.order.system.domain.valueobject.CustomerId;
import com.food.ordering.order.system.domain.valueobject.Money;
import com.food.ordering.order.system.domain.valueobject.ProductId;
import com.food.ordering.order.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand){
        return Restaurant.builder().
                restaurantId(new RestaurantId(createOrderCommand.getRestaurantId())).
                products(createOrderCommand.getOrderItems().stream().map(orderItem ->
                        new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand){
        return Order.builder().
                customerId(new CustomerId(createOrderCommand.getCustomerId())).
                restaurantId(new RestaurantId(createOrderCommand.getRestaurantId())).
                deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress())).
                price(new Money(createOrderCommand.getPrice())).
                orderItems(orderItemsToOrderItemEntities(createOrderCommand.getOrderItems())).
                build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.ordering.order.system.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem ->
                OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
