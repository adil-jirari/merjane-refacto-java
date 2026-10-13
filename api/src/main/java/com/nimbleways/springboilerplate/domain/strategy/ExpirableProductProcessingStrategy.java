package com.nimbleways.springboilerplate.domain.strategy;


import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductDecreasedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductExpiredEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.nimbleways.springboilerplate.domain.Constants.UTC;

public class ExpirableProductProcessingStrategy implements ProductProcessingStrategy {

    @Override
    public ProductProcessedEvent process(Product product) {
        LocalDate today = LocalDate.now(ZoneId.of(UTC));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(UTC));
        if (product.hasAvailableStock() && product.isNotExpired(today)) {
            product.decreaseAvailableStock();
            return new ProductDecreasedEvent(product, now);
        } else {
            product.markAsUnavailable();
            return new ProductExpiredEvent(product, now);
        }
    }
}