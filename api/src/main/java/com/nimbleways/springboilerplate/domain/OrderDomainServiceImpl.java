package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.strategy.*;

import java.util.List;

public class OrderDomainServiceImpl implements OrderDomainService {

    private final ProductProcessingStrategyResolver strategyResolver;

    public OrderDomainServiceImpl(ProductProcessingStrategyResolver strategyResolver) {
        this.strategyResolver = strategyResolver;
    }

    @Override
    public List<ProductProcessedEvent> validateAndProcessOrder(Order order) {
        order.validate();
        return order.getProducts()
                .stream()
                .map(this::process)
                .toList();
    }

    private ProductProcessedEvent process(Product product) {

        ProductProcessingStrategy strategy =
                strategyResolver.resolve(product.getType());

        return strategy.process(product);
    }
}
