package com.food.ordering.system.payment.service.dataaccess.outbox.mapper;

import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxEntity orderOutboxMessageToOrderOutboxEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .paymentStatus(orderOutboxMessage.getPaymentStatus())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .processedAt(orderOutboxMessage.getProcessedAt())
                .version(orderOutboxMessage.getVersion())
                .build();
    }

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity orderOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(orderOutboxEntity.getId())
                .sagaId(orderOutboxEntity.getSagaId())
                .type(orderOutboxEntity.getType())
                .payload(orderOutboxEntity.getPayload())
                .outboxStatus(orderOutboxEntity.getOutboxStatus())
                .paymentStatus(orderOutboxEntity.getPaymentStatus())
                .createdAt(orderOutboxEntity.getCreatedAt())
                .processedAt(orderOutboxEntity.getProcessedAt())
                .version(orderOutboxEntity.getVersion())
                .build();
    }
}
