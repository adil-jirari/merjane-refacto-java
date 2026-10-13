package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductDecreasedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductDelayedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductOutOfStockEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class SeasonalProductProcessingStrategyTest {

    private final SeasonalProductProcessingStrategy strategy =
            new SeasonalProductProcessingStrategy();

    @Test
    void shouldDecreaseStockWhenProductIsInSeasonAndAvailable() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Watermelon")
                .type(ProductType.SEASONAL)
                .available(10)
                .leadTime(15)
                .seasonStartDate(LocalDate.now().minusDays(5))
                .seasonEndDate(LocalDate.now().plusDays(5))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductDecreasedEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(9, product.getAvailable());
    }

    @Test
    void shouldReturnOutOfStockEventWhenRestockWouldBeAfterSeasonEnd() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Watermelon")
                .type(ProductType.SEASONAL)
                .available(0)
                .leadTime(30)
                .seasonStartDate(LocalDate.now().minusDays(20))
                .seasonEndDate(LocalDate.now().plusDays(5))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductOutOfStockEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, product.getAvailable());
    }

    @Test
    void shouldReturnOutOfStockEventWhenSeasonHasNotStartedYet() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Grapes")
                .type(ProductType.SEASONAL)
                .available(10)
                .leadTime(5)
                .seasonStartDate(LocalDate.now().plusDays(30))
                .seasonEndDate(LocalDate.now().plusDays(60))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductOutOfStockEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void shouldReturnDelayedEventWhenProductCannotBeRestockedBeforeSeasonEnd() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Watermelon")
                .type(ProductType.SEASONAL)
                .available(0)
                .leadTime(5)
                .seasonStartDate(LocalDate.now().minusDays(30))
                .seasonEndDate(LocalDate.now().plusDays(30))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductDelayedEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
    }
}