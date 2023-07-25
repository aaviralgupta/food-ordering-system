package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;

import static com.food.ordering.system.domain.DomainConstants.UTC;

public class CustomerDomainServiceImpl implements CustomerDomainService{
    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        validateUserName(customer.getUsername());
        validateFirstName(customer.getFirstName());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateFirstName(String firstName) {
        if (firstName.length()<2)
            throw new CustomerDomainException("First name should contain at least 2 chars");
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(firstName).matches())
            throw new CustomerDomainException("First name should contains characters & numerals");
    }

    private void validateUserName(String username) {
        if (username.length()<2)
            throw new CustomerDomainException("Username should contain at least 2 chars");
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(username).matches())
            throw new CustomerDomainException("Username should contains characters & numerals");
    }
}
