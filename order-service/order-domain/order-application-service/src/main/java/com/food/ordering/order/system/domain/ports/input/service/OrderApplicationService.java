package com.food.ordering.order.system.domain.ports.input.service;

import com.food.ordering.order.system.domain.dto.create.CreateOrderCommand;
import com.food.ordering.order.system.domain.dto.create.CreateOrderResponse;
import com.food.ordering.order.system.domain.dto.track.TrackOrderQuery;
import com.food.ordering.order.system.domain.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

}
