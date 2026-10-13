package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.nimbleways.springboilerplate.domain.Constants.UTC;

public class SeasonalProductProcessingStrategy implements ProductProcessingStrategy {

    @Override
    public ProductProcessedEvent process(Product product) {
        LocalDate today = LocalDate.now(ZoneId.of(UTC));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(UTC));
        if (product.isCurrentlyInSeason(today) && product.hasAvailableStock()) {
            product.decreaseAvailableStock();
            return new ProductDecreasedEvent(product, now);
        } else if (product.isRestockAfterSeasonEnd(today)) {
            product.markAsUnavailable();
            return new ProductOutOfStockEvent(product, now);
        } else if (product.isBeforeSeasonStart(today)) {
            return new ProductOutOfStockEvent(product, now);
        } else {
            return new ProductDelayedEvent(product, now);
        }
    }
}