package com.nimbleways.springboilerplate.domain.event;

import com.nimbleways.springboilerplate.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductDecreasedEvent extends ProductProcessedEvent {
    public ProductDecreasedEvent(Product product, ZonedDateTime createdAt) {
        super(product, createdAt);
    }
}
