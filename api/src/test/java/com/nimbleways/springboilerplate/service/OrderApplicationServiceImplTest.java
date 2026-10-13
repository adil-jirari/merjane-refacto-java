package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.OrderDomainService;
import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.service.dto.request.OrderProcessCommand;
import com.nimbleways.springboilerplate.service.dto.response.OrderProcessResponse;
import com.nimbleways.springboilerplate.service.ports.input.service.ProductEventPublisher;
import com.nimbleways.springboilerplate.service.ports.output.repository.OrderRepository;
import com.nimbleways.springboilerplate.service.ports.output.repository.ProductRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class OrderApplicationServiceImplTest {

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEventPublisher productEventPublisher;

    @InjectMocks
    private OrderApplicationServiceImpl service;

    @Mock
    private Order order;

    @Mock
    private Product product1;

    @Mock
    private Product product2;

    @Mock
    private ProductProcessedEvent event1;

    @Mock
    private ProductProcessedEvent event2;

    @Test
    void shouldProcessOrderSuccessfully() {
        UUID orderId = UUID.randomUUID();

        OrderProcessCommand command =
                new OrderProcessCommand(orderId);

        when(orderRepository.findById(new OrderId(orderId)))
                .thenReturn(Optional.of(order));

        when(order.getId())
                .thenReturn(new OrderId(orderId));

        when(event1.getProduct())
                .thenReturn(product1);

        when(event2.getProduct())
                .thenReturn(product2);

        when(orderDomainService.validateAndProcessOrder(order))
                .thenReturn(List.of(event1, event2));

        OrderProcessResponse response =
                service.processOrder(command);

        assertNotNull(response);
        assertEquals(orderId, response.orderId());

        verify(orderRepository)
                .findById(any(OrderId.class));

        verify(orderDomainService)
                .validateAndProcessOrder(order);

        verify(productRepository).save(product1);
        verify(productRepository).save(product2);

        verify(productEventPublisher).publish(event1);
        verify(productEventPublisher).publish(event2);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        OrderProcessCommand command =
                new OrderProcessCommand(orderId);

        when(orderRepository.findById(any(OrderId.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> service.processOrder(command)
        );

        verify(orderRepository)
                .findById(any(OrderId.class));

        verifyNoInteractions(
                orderDomainService,
                productRepository,
                productEventPublisher
        );
    }

    @Test
    void shouldProcessOrderWithNoEvents() {
        UUID orderId = UUID.randomUUID();

        OrderProcessCommand command =
                new OrderProcessCommand(orderId);

        when(orderRepository.findById(any(OrderId.class)))
                .thenReturn(Optional.of(order));

        when(order.getId())
                .thenReturn(new OrderId(orderId));

        when(orderDomainService.validateAndProcessOrder(order))
                .thenReturn(List.of());

        OrderProcessResponse response =
                service.processOrder(command);

        assertNotNull(response);
        assertEquals(orderId, response.orderId());

        verify(orderDomainService)
                .validateAndProcessOrder(order);

        verifyNoInteractions(
                productRepository,
                productEventPublisher
        );
    }
}