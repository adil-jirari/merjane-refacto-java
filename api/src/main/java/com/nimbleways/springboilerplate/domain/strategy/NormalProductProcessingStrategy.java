package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductDecreasedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductDelayedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.nimbleways.springboilerplate.domain.Constants.UTC;

public class NormalProductProcessingStrategy implements ProductProcessingStrategy {

    @Override
    public ProductProcessedEvent process(Product product) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(UTC));
        if (product.hasAvailableStock()) {
            product.decreaseAvailableStock();
            return new ProductDecreasedEvent(product, now);
        } else if (product.hasPositiveLeadTime()) {
            return new ProductDelayedEvent(product, now);
        }
        throw new UnsupportedProductException("Normal Product with Id: " + product.getId().getValue() + " cannot be processed!");
    }
}
