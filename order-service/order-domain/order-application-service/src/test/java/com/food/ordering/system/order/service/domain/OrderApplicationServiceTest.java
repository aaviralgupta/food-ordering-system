package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest (classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("67028a04-727f-4b1c-b11c-92835e4d4e04");
    private final UUID RESTAURANT_ID = UUID.fromString("cd49fdcd-6707-4aa6-b356-d8b157eff3f0");
    private final UUID PRODUCT_ID = UUID.fromString("a546f25b-03dd-4509-9dab-f690668ced60");
    private final UUID PRODUCT_ID2 = UUID.fromString("c576db9a-be7a-462c-8bfb-4cd50a472a86");
    private final UUID ORDER_ID = UUID.fromString("8c23c8c2-18ff-4070-a9cb-600b8c2cf51e");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init(){
        createOrderCommand=CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street1")
                        .city("city1")
                        .postalCode("1000AB")
                        .build())
                .price(PRICE)
                .orderItems(List.of(OrderItem.builder()
                            .productId(PRODUCT_ID)
                            .quantity(1)
                            .price(new BigDecimal("50.00"))
                            .subTotal(new BigDecimal("50.00"))
                            .build(),
                        OrderItem.builder()
                            .productId(PRODUCT_ID2)
                            .quantity(3)
                            .price(new BigDecimal("50.00"))
                            .subTotal(new BigDecimal("150.00"))
                            .build()))
                .build();

        createOrderCommandWrongPrice=CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street1")
                        .city("city1")
                        .postalCode("1000AB")
                        .build())
                .price(new BigDecimal("250.00"))
                .orderItems(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice=CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street1")
                        .city("city1")
                        .postalCode("1000AB")
                        .build())
                .price(new BigDecimal("80.00"))
                .orderItems(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("20.00"))
                                .subTotal(new BigDecimal("20.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("20.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse= Restaurant.builder()
                .restaurantId(new RestaurantId(RESTAURANT_ID))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),"product-1",new Money(new BigDecimal("50.00")))
                        ,new Product(new ProductId(PRODUCT_ID2),"product-2",new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Order order= orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));
        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(createOrderResponse.getOrderStatus(), OrderStatus.PENDING);
        assertEquals(createOrderResponse.getMessage(),"Order Created Successfully");
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice(){
        assertThrows(OrderDomainException.class,() -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
    }

    @Test
    public void testCreateOrderWithWrongPrice(){
        assertThrows(OrderDomainException.class,() -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
    }



}
