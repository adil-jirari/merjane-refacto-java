package com.nimbleways.springboilerplate.domain.event;

import com.nimbleways.springboilerplate.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductOutOfStockEvent extends ProductProcessedEvent {

    public ProductOutOfStockEvent(Product product, ZonedDateTime createdAt) {
        super(product, createdAt);
    }
}
