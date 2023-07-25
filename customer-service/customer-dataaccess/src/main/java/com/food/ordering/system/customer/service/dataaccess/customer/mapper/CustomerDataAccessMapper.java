package com.food.ordering.system.customer.service.dataaccess.customer.mapper;

import com.food.ordering.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()),
                customerEntity.getFirstName(),
                customerEntity.getLastName(),
                customerEntity.getUsername());
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .username(customer.getUsername())
                .build();
    }
}
