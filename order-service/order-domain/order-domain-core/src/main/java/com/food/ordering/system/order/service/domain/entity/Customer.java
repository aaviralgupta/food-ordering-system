package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    private String firstName;
    private String lastName;
    private String username;

    public Customer(CustomerId customerId, String firstName, String lastName, String username) {
        super.setId(customerId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }
}
