package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantResponseMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderOutboxScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantResponseMessagePublisher restaurantResponseMessagePublisher;

    public OrderOutboxScheduler(OrderOutboxHelper orderOutboxHelper,
                                RestaurantResponseMessagePublisher restaurantResponseMessagePublisher) {
        this.orderOutboxHelper = orderOutboxHelper;
        this.restaurantResponseMessagePublisher = restaurantResponseMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${restaurant-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${restaurant-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> orderOutboxMessageResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.STARTED);
        if(orderOutboxMessageResponse.isPresent() && orderOutboxMessageResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = orderOutboxMessageResponse.get();
            log.info("Received  {} OrderOutboxMessages with ids {}, sending to kafka!", outboxMessages.size(),
                    outboxMessages.stream().map(OrderOutboxMessage::getId)
                            .map(UUID::toString).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage ->
                    restaurantResponseMessagePublisher.publish(outboxMessage, orderOutboxHelper::updateOutboxStatus));
            log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
        }

    }
}
