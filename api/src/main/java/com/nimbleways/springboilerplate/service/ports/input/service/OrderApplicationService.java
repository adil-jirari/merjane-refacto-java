package com.nimbleways.springboilerplate.service.ports.input.service;

import com.nimbleways.springboilerplate.service.dto.request.OrderProcessCommand;
import com.nimbleways.springboilerplate.service.dto.response.OrderProcessResponse;

public interface OrderApplicationService {

    OrderProcessResponse processOrder(OrderProcessCommand command);
}
