package com.nimbleways.springboilerplate.services.implementations;

import java.util.List;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.implementations.product.ProductProcessingStrategy;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final List<ProductProcessingStrategy> strategies;

    public ProductService(List<ProductProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public void process(Product product) {
        strategies.stream()
                .filter(strategy -> strategy.supports(product))
                .findFirst()
                .ifPresent(strategy -> strategy.process(product));
    }
}