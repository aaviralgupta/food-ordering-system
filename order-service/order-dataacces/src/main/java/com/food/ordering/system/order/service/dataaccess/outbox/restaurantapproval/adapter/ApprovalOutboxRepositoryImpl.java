package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxDataMapper approvalOutboxDataMapper;
    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;

    public ApprovalOutboxRepositoryImpl(ApprovalOutboxDataMapper approvalOutboxDataMapper,
                                        ApprovalOutboxJpaRepository approvalOutboxJpaRepository) {
        this.approvalOutboxDataMapper = approvalOutboxDataMapper;
        this.approvalOutboxJpaRepository = approvalOutboxJpaRepository;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxDataMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(
                approvalOutboxJpaRepository.save(approvalOutboxDataMapper
                        .orderApprovalOutboxMessageToApprovalOutboxEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                             OutboxStatus outboxStatus,
                                                                                             SagaStatus... sagaStatus) {
        List<SagaStatus> sagaStatusList = Arrays.asList(sagaStatus);
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatus(type,
                        outboxStatus, sagaStatusList)
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object could not" +
                        " be found for saga Type :" + type))
                .stream()
                .map(approvalOutboxDataMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                                 UUID sagaId,
                                                                                 SagaStatus... sagaStatus) {
        List<SagaStatus> sagaStatusList = Arrays.asList(sagaStatus);
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatus(type, sagaId, sagaStatusList)
                .map(approvalOutboxDataMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type,
                                                         OutboxStatus outboxStatus,
                                                         SagaStatus... sagaStatus) {
        List<SagaStatus> sagaStatusList = Arrays.asList(sagaStatus);
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatus(type, outboxStatus, sagaStatusList);
    }
}
