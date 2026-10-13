package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductDecreasedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductDelayedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class NormalProductProcessingStrategyTest {

    private final NormalProductProcessingStrategy strategy =
            new NormalProductProcessingStrategy();

    @Test
    void shouldDecreaseStockWhenProductHasAvailableStock() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("USB Cable")
                .type(ProductType.NORMAL)
                .available(10)
                .leadTime(15)
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductDecreasedEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(9, product.getAvailable());
    }

    @Test
    void shouldReturnDelayedEventWhenNoStockButLeadTimeIsPositive() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("USB Dongle")
                .type(ProductType.NORMAL)
                .available(0)
                .leadTime(10)
                .build();

        ProductProcessedEvent result = strategy.process(product);

        assertInstanceOf(ProductDelayedEvent.class, result);
        assertEquals(product, result.getProduct());
        assertNotNull(result.getCreatedAt());
        assertEquals(0, product.getAvailable());
    }

    @Test
    void shouldThrowExceptionWhenNoStockAndLeadTimeIsNotPositive() {
        Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .name("USB Dongle")
                .type(ProductType.NORMAL)
                .available(0)
                .leadTime(0)
                .build();

        UnsupportedProductException exception = assertThrows(
                UnsupportedProductException.class,
                () -> strategy.process(product)
        );

        assertTrue(
                exception.getMessage().contains(
                        product.getId().getValue().toString()
                )
        );
    }
}