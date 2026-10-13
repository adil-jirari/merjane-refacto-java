package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;

public interface ProductProcessingStrategy {

    ProductProcessedEvent process(Product product);
}
