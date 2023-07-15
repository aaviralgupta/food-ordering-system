package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

public class PaymentOutboxDataMapper {

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage (PaymentOutboxEntity
                                                                                             paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .version(paymentOutboxEntity.getVersion())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .processedAt(paymentOutboxEntity.getProcessedAt())
                .payload(paymentOutboxEntity.getPayload())
                .type(paymentOutboxEntity.getType())
                .build();
    }

    public PaymentOutboxEntity orderPaymentOutboxMessageToPaymentOutboxEntity (OrderPaymentOutboxMessage
                                                                                       orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .processedAt(orderPaymentOutboxMessage.getProcessedAt())
                .payload(orderPaymentOutboxMessage.getPayload())
                .type(orderPaymentOutboxMessage.getType())
                .build();
    }
}
