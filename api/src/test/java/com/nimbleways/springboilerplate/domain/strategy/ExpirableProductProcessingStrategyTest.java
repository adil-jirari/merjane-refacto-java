package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductDecreasedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductExpiredEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class ExpirableProductProcessingStrategyTest {

    private final ExpirableProductProcessingStrategy strategy =
            new ExpirableProductProcessingStrategy();

    @Test
    void shouldDecreaseStockWhenProductHasStockAndIsNotExpired() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Butter")
                .type(ProductType.EXPIRABLE)
                .available(10)
                .leadTime(5)
                .expiryDate(LocalDate.now().plusDays(5))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductDecreasedEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(9, product.getAvailable());
    }

    @Test
    void shouldMarkProductAsUnavailableWhenExpired() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Milk")
                .type(ProductType.EXPIRABLE)
                .available(10)
                .leadTime(5)
                .expiryDate(LocalDate.now().minusDays(1))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductExpiredEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, product.getAvailable());
    }

    @Test
    void shouldMarkProductAsUnavailableWhenNoStockAvailable() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("Milk")
                .type(ProductType.EXPIRABLE)
                .available(0)
                .leadTime(5)
                .expiryDate(LocalDate.now().plusDays(10))
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductExpiredEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, product.getAvailable());
    }
}