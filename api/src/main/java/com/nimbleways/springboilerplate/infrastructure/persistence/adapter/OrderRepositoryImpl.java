package com.nimbleways.springboilerplate.infrastructure.persistence.adapter;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.infrastructure.persistence.mapper.OrderDataAccessMapper;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.OrderJpaRepository;
import com.nimbleways.springboilerplate.service.ports.output.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findAggregateById(orderId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(
                orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order)));
    }
}
