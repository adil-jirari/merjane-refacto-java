package com.nimbleways.springboilerplate.domain.event;

import com.nimbleways.springboilerplate.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductDelayedEvent extends ProductProcessedEvent{

    public ProductDelayedEvent(Product product, ZonedDateTime createdAt) {
        super(product, createdAt);
    }
}
