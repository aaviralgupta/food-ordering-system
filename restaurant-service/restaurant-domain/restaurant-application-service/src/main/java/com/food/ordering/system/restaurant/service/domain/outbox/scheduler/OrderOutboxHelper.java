package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantDomainException;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;
import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;

@Slf4j
@Component
public class OrderOutboxHelper {
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    public OrderOutboxHelper(OrderOutboxRepository orderOutboxRepository, ObjectMapper objectMapper) {
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID sagaId,
                                                                                                OutboxStatus
                                                                                                    outboxStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(ORDER_SAGA_NAME, sagaId, outboxStatus);
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus status) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, status);
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus status) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, status);
    }

    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                       OrderApprovalStatus approvalStatus,
                                       OutboxStatus outboxStatus,
                                       UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .type(ORDER_SAGA_NAME)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .payload(createEventPayload(orderEventPayload))
                .outboxStatus(outboxStatus)
                .approvalStatus(approvalStatus)
                .sagaId(sagaId)
                .build());
    }

    @Transactional
    public void updateOutboxStatus(OrderOutboxMessage outboxMessage, OutboxStatus status) {
        outboxMessage.setOutboxStatus(status);
        save(outboxMessage);
    }

    private String createEventPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new RestaurantDomainException("Could not create OrderEventPayload json!", e);
        }
    }

    private void save(OrderOutboxMessage outboxMessage) {
        OrderOutboxMessage orderOutboxMessageResponse = orderOutboxRepository.save(outboxMessage);
        if(orderOutboxMessageResponse == null) {
            log.error("Could not save Outbox Message!");
            throw new RestaurantDomainException("Could not save Outbox Message!");
        }
        log.info("Order outbox message is saved with id : {}", outboxMessage.getId());
    }
}
