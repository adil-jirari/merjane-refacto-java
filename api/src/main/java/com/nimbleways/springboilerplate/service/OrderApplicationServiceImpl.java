package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.OrderDomainService;
import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.service.dto.request.OrderProcessCommand;
import com.nimbleways.springboilerplate.service.dto.response.OrderProcessResponse;
import com.nimbleways.springboilerplate.service.ports.input.service.OrderApplicationService;
import com.nimbleways.springboilerplate.service.ports.input.service.ProductEventPublisher;
import com.nimbleways.springboilerplate.service.ports.output.repository.OrderRepository;
import com.nimbleways.springboilerplate.service.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Validated
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;

    public OrderApplicationServiceImpl(OrderDomainService orderDomainService,
                                       OrderRepository orderRepository,
                                       ProductRepository productRepository,
                                       ProductEventPublisher productEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productEventPublisher = productEventPublisher;
    }

    @Override
    @Transactional
    public OrderProcessResponse processOrder(OrderProcessCommand command) {
        log.info("Processing order {}", command);
        Order order = orderRepository.findById(new OrderId(command.getOrderId()))
                    .orElseThrow(() -> {
                        log.error("Order not found: {}", command.getOrderId());
                        return new OrderNotFoundException("Order not found: " + command.getOrderId());
                    });

        List<ProductProcessedEvent> events = orderDomainService.validateAndProcessOrder(order);
        log.info("Order processed successfully: {}", command.getOrderId());
        for (ProductProcessedEvent event : events) {
            productRepository.save(event.getProduct());
        }
        for (ProductProcessedEvent event : events) {
            productEventPublisher.publish(event);
        }
        return new OrderProcessResponse(order.getId().getValue());
    }
}
