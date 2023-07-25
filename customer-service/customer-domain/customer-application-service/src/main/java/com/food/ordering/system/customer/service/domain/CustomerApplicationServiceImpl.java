package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerDataMapper customerDataMapper;
    private final CreateCustomerCommandHandler createCustomerCommandHandler;
    private final CustomerMessagePublisher customerMessagePublisher;

    public CustomerApplicationServiceImpl(CustomerDataMapper customerDataMapper,
                                          CreateCustomerCommandHandler createCustomerCommandHandler,
                                          CustomerMessagePublisher customerMessagePublisher) {
        this.customerDataMapper = customerDataMapper;
        this.createCustomerCommandHandler = createCustomerCommandHandler;
        this.customerMessagePublisher = customerMessagePublisher;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent createdEvent = createCustomerCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(createdEvent);
        return customerDataMapper.customerToCreateCustomerResponse(createdEvent.getCustomer(),
                "Customer saved successfully!");
    }
}
