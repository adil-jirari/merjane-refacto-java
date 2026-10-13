package com.nimbleways.springboilerplate.domain.event;

import com.nimbleways.springboilerplate.common.domain.event.DomainEvent;
import com.nimbleways.springboilerplate.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductProcessedEvent implements DomainEvent<Product> {

    private final Product product;
    private final ZonedDateTime createdAt;

    public ProductProcessedEvent(Product product, ZonedDateTime createdAt) {
        this.product = product;
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
