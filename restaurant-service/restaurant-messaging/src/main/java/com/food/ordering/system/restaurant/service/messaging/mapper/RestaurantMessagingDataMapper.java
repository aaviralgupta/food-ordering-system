package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.valueobject.ProductId;
import com.food.ordering.system.order.service.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.food.ordering.system.order.service.domain.DomainConstants.UTC;

@Component
public class RestaurantMessagingDataMapper {
    public RestaurantApprovalResponseAvroModel
    orderApprovedEventToRestaurantApprovalResponseAvroModel(OrderApprovedEvent orderApprovedEvent){
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderApprovedEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(orderApprovedEvent.getOrderApproval().getRestaurantId().getValue().toString())
                .setOrderApprovalStatus(OrderApprovalStatus
                        .valueOf(orderApprovedEvent.getOrderApproval().getApprovalStatus().name()))
                .setCreatedAt(ZonedDateTime.now(ZoneId.of(UTC)).toInstant())
                .setFailureMessages(orderApprovedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent){
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(orderRejectedEvent.getOrderApproval().getRestaurantId().getValue().toString())
                .setOrderApprovalStatus(OrderApprovalStatus
                        .valueOf(orderRejectedEvent.getOrderApproval().getApprovalStatus().name()))
                .setCreatedAt(ZonedDateTime.now(ZoneId.of(UTC)).toInstant())
                .setFailureMessages(orderRejectedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(RestaurantApprovalRequestAvroModel
                                                                          restaurantApprovalRequestAvroModel){
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts()
                        .stream().map(model ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(model.getId())))
                                        .quantity(model.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }
}
