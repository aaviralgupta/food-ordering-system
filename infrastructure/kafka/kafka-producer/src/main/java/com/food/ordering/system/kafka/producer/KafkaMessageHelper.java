package com.food.ordering.system.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public KafkaMessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T, U>BiConsumer<SendResult<String, T>, Throwable>
    getKafkaCallback(String paymentResponseTopicName, T avroModel,U outboxMessage,
                     BiConsumer<U, OutboxStatus> outboxCallback,
                     String orderId, String avroModelName) {

        return (result, ex) -> {
            if(ex==null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successfully response from kafka for order id: {} Topic : {} " +
                                "Partition :{} Offset: {} Timestamp:{}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.partition());
                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);

            } else {
                log.error("Error while sending {} message {} and outbox type : {} to topic {}",avroModelName,
                        avroModel.toString(), outboxMessage.getClass().getName(),paymentResponseTopicName, ex);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }
        };
    }

    public <T> T getOutboxMessageEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could bot read {} object!",outputType.getName(), e);
            throw new OrderDomainException("Could bot read "+ outputType.getName() +" object!", e);
        }
    }
}
