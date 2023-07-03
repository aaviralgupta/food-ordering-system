package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant){
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntitiesToRestaurant(List<RestaurantEntity> restaurantEntities){
        RestaurantEntity restaurantEntity= restaurantEntities.stream()
                .findFirst().orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                Product.builder()
                        .productId(new ProductId(entity.getProductId()))
                        .name(entity.getProductName())
                        .price(new Money(entity.getProductPrice()))
                        .available(entity.getProductAvailable())
                        .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .orderDetail(OrderDetail.builder()
                        .products(restaurantProducts)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval){
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(orderApprovalEntity.getId()))
                .orderId(new OrderId(orderApprovalEntity.getOrderId()))
                .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }


}
