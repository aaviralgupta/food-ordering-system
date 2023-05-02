package com.food.ordering.order.system.domain;

import com.food.ordering.order.system.domain.dto.create.CreateOrderCommand;
import com.food.ordering.order.system.domain.dto.create.CreateOrderResponse;
import com.food.ordering.order.system.domain.mapper.OrderDataMapper;
import com.food.ordering.order.system.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.order.system.domain.ports.output.repository.OrderRepository;
import com.food.ordering.order.system.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final CustomerRepository customerRepository;

    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     RestaurantRepository restaurantRepository,
                                     CustomerRepository customerRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult=saveOrder(order);
        log.info("Order is created with Id : {}",orderResult.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(orderResult);
    }



    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
       Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
       Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if(optionalRestaurant.isEmpty()){
            log.warn("Could not find restaurant with id : {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with id : " + createOrderCommand.getRestaurantId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if(customer.isEmpty()){
            log.warn("Could not find customer with id : {}",customerId);
            throw new OrderDomainException("Could not find customer with id : "+customerId);
        }
    }

    private Order saveOrder(Order order){
        Order orderResult= orderRepository.save(order);
        if(orderResult== null){
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order saved with id :{}",orderResult.getId().getValue());
        return orderResult;
    }


}
