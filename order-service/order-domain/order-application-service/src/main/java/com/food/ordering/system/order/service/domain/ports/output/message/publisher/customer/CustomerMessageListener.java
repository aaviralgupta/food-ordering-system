package com.food.ordering.system.order.service.domain.ports.output.message.publisher.customer;

import com.food.ordering.system.order.service.domain.dto.create.CustomerModel;

public interface CustomerMessageListener {
    void customerCreated(CustomerModel customerModel);
}
