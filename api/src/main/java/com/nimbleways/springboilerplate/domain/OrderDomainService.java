package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;

import java.util.List;

public interface OrderDomainService {

    List<ProductProcessedEvent> validateAndProcessOrder(Order order);

}
