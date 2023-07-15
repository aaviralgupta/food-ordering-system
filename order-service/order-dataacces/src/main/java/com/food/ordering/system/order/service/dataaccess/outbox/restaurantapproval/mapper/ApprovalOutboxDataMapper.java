package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;

public class ApprovalOutboxDataMapper {
    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(ApprovalOutboxEntity
                                                                                             approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
                .id(approvalOutboxEntity.getId())
                .sagaId(approvalOutboxEntity.getSagaId())
                .outboxStatus(approvalOutboxEntity.getOutboxStatus())
                .sagaStatus(approvalOutboxEntity.getSagaStatus())
                .orderStatus(approvalOutboxEntity.getOrderStatus())
                .version(approvalOutboxEntity.getVersion())
                .createdAt(approvalOutboxEntity.getCreatedAt())
                .processedAt(approvalOutboxEntity.getProcessedAt())
                .payload(approvalOutboxEntity.getPayload())
                .type(approvalOutboxEntity.getType())
                .build();
    }

    public ApprovalOutboxEntity orderApprovalOutboxMessageToApprovalOutboxEntity(OrderApprovalOutboxMessage
                                                                                       orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .processedAt(orderApprovalOutboxMessage.getProcessedAt())
                .payload(orderApprovalOutboxMessage.getPayload())
                .type(orderApprovalOutboxMessage.getType())
                .build();
    }
}
