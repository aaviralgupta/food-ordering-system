package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
}
