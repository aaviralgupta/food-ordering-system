package com.food.ordering.system.customer.service.messaging.publisher.kafka;

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.food.ordering.system.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;

    public CustomerCreatedEventKafkaPublisher(CustomerMessagingDataMapper customerMessagingDataMapper,
                                              KafkaProducer<String, CustomerAvroModel> kafkaProducer,
                                              CustomerServiceConfigData customerServiceConfigData) {
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.customerServiceConfigData = customerServiceConfigData;
    }

    @Override
    public void publish(CustomerCreatedEvent createdEvent) {
        try{
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper
                    .customerCreatedEventToCustomerAvroModel(createdEvent);
            kafkaProducer.send(customerServiceConfigData.getCustomerTopicName(), customerAvroModel.getId(),
                    customerAvroModel,
                    getCallback(customerAvroModel));
            log.info("Customer sent to Kafka with customer id : {}", customerAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending message to kafka for customer id : {}, error : {}",
                    createdEvent.getCustomer().getId().getValue().toString(), e.getMessage());
        }
    }

    private BiConsumer<SendResult<String, CustomerAvroModel>, Throwable>
    getCallback(CustomerAvroModel customerAvroModel) {

        return (result, ex) -> {
            if(ex==null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received new metadata . Topic : {}, Partition: {}, Offset: {} ",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset());

            } else {
                log.error("Error while sending message for id: {} to topic : {}", customerAvroModel.getId(),
                        customerServiceConfigData.getCustomerTopicName());
            }
        };
    }
}
