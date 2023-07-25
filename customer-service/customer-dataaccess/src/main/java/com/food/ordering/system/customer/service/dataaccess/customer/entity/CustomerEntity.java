package com.food.ordering.system.customer.service.dataaccess.customer.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "customers", schema = "customer")
@Entity
public class CustomerEntity {

    @Id
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
}
