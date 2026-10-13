package com.nimbleways.springboilerplate.infrastructure.persistence.adapter;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.mapper.OrderDataAccessMapper;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.OrderJpaRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class OrderRepositoryImplTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private OrderDataAccessMapper orderDataAccessMapper;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    @Mock
    private Order order;

    @Mock
    private OrderEntity orderEntity;

    @Test
    void shouldFindOrderById() {
        UUID orderUuid = UUID.randomUUID();
        OrderId orderId = new OrderId(orderUuid);

        when(orderJpaRepository.findAggregateById(orderUuid))
                .thenReturn(Optional.of(orderEntity));

        when(orderDataAccessMapper.orderEntityToOrder(orderEntity))
                .thenReturn(order);

        Optional<Order> result = orderRepository.findById(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());

        verify(orderJpaRepository).findAggregateById(orderUuid);
        verify(orderDataAccessMapper).orderEntityToOrder(orderEntity);
    }

    @Test
    void shouldReturnEmptyWhenOrderIsNotFound() {
        UUID orderUuid = UUID.randomUUID();
        OrderId orderId = new OrderId(orderUuid);

        when(orderJpaRepository.findAggregateById(orderUuid))
                .thenReturn(Optional.empty());

        Optional<Order> result = orderRepository.findById(orderId);

        assertTrue(result.isEmpty());

        verify(orderJpaRepository).findAggregateById(orderUuid);
        verifyNoInteractions(orderDataAccessMapper);
    }

    @Test
    void shouldSaveOrder() {
        when(orderDataAccessMapper.orderToOrderEntity(order))
                .thenReturn(orderEntity);

        when(orderJpaRepository.save(orderEntity))
                .thenReturn(orderEntity);

        when(orderDataAccessMapper.orderEntityToOrder(orderEntity))
                .thenReturn(order);

        Order result = orderRepository.save(order);

        assertNotNull(result);
        assertEquals(order, result);

        verify(orderDataAccessMapper)
                .orderToOrderEntity(order);

        verify(orderJpaRepository)
                .save(orderEntity);

        verify(orderDataAccessMapper)
                .orderEntityToOrder(orderEntity);
    }
}