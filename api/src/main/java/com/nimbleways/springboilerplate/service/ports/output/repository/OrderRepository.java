package com.nimbleways.springboilerplate.service.ports.output.repository;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(OrderId orderId);

    Order save(Order order);

}
