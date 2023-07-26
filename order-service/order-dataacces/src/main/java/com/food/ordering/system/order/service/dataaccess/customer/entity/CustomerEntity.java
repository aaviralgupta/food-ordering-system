package com.food.ordering.system.order.service.dataaccess.customer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "customers")
@Entity
public class CustomerEntity {
    @Id
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;

}
