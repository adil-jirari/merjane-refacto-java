package com.nimbleways.springboilerplate.domain.event;

import com.nimbleways.springboilerplate.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductExpiredEvent extends ProductProcessedEvent {

    public ProductExpiredEvent(Product product, ZonedDateTime createdAt) {
        super(product, createdAt);
    }
}
