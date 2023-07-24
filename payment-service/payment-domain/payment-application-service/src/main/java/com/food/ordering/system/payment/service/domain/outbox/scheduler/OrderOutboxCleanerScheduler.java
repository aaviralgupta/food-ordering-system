package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {
    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> orderOutboxMessageResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if(orderOutboxMessageResponse.isPresent() && orderOutboxMessageResponse.get().size() > 0) {
            List<OrderOutboxMessage> orderOutboxMessages = orderOutboxMessageResponse.get();
            log.info("Received {} OrderOutboxMessage for cleanup,The payloads: {}",
                    orderOutboxMessages.size(),
                    orderOutboxMessages.stream().map(OrderOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("{} OrderOutboxMessage deleted!",orderOutboxMessages.size());
        }

    }
}
