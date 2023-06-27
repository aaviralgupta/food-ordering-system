package com.food.ordering.system.payment.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel
    paymentCompletedEventTopaymentResponseAvroModel(PaymentCompletedEvent paymentCompletedEvent){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCompletedEvent.getPayment().getId().getValue().toString())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCompletedEvent.getPayment().getPaymentStatus().name()))
                .setCustomerId(paymentCompletedEvent.getPayment().getCustomerId().getValue().toString())
                .setPrice(paymentCompletedEvent.getPayment().getPrice().getAmount())
                .setFailureMessages(paymentCompletedEvent.getFailureMessages())
                .setCreatedAt(paymentCompletedEvent.getCreatedAt().toInstant())
                .build();
    }

    public PaymentResponseAvroModel
    paymentCancelledEventTopaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentCancelledEvent.getPayment().getId().getValue().toString())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.getPayment().getPaymentStatus().name()))
                .setCustomerId(paymentCancelledEvent.getPayment().getCustomerId().getValue().toString())
                .setPrice(paymentCancelledEvent.getPayment().getPrice().getAmount())
                .setFailureMessages(paymentCancelledEvent.getFailureMessages())
                .setCreatedAt(paymentCancelledEvent.getCreatedAt().toInstant())
                .build();
    }

    public PaymentResponseAvroModel
    paymentFailedEventTopaymentResponseAvroModel(PaymentFailedEvent paymentFailedEvent){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentFailedEvent.getPayment().getId().getValue().toString())
                .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.getPayment().getPaymentStatus().name()))
                .setCustomerId(paymentFailedEvent.getPayment().getCustomerId().getValue().toString())
                .setPrice(paymentFailedEvent.getPayment().getPrice().getAmount())
                .setFailureMessages(paymentFailedEvent.getFailureMessages())
                .setCreatedAt(paymentFailedEvent.getCreatedAt().toInstant())
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel){
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaId(paymentRequestAvroModel.getSagaId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }
}
