package com.food.ordering.system.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.customer.service.domain.CustomerDomainService;
import com.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService orderDomainService(){
        return new CustomerDomainServiceImpl();
    }
}
