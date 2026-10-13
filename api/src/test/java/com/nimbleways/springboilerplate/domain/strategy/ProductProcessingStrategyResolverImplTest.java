package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class ProductProcessingStrategyResolverImplTest {

    private final ProductProcessingStrategyResolver resolver =
            new ProductProcessingStrategyResolverImpl();

    @Test
    void shouldResolveNormalStrategy() {
        ProductProcessingStrategy strategy =
                resolver.resolve(ProductType.NORMAL);

        assertNotNull(strategy);
        assertInstanceOf(
                NormalProductProcessingStrategy.class,
                strategy
        );
    }

    @Test
    void shouldResolveExpirableStrategy() {
        ProductProcessingStrategy strategy =
                resolver.resolve(ProductType.EXPIRABLE);

        assertNotNull(strategy);
        assertInstanceOf(
                ExpirableProductProcessingStrategy.class,
                strategy
        );
    }

    @Test
    void shouldResolveSeasonalStrategy() {
        ProductProcessingStrategy strategy =
                resolver.resolve(ProductType.SEASONAL);

        assertNotNull(strategy);
        assertInstanceOf(
                SeasonalProductProcessingStrategy.class,
                strategy
        );
    }

    @Test
    void shouldThrowExceptionWhenProductTypeIsNull() {
        OrderDomainException exception = assertThrows(
                OrderDomainException.class,
                () -> resolver.resolve(null)
        );

        assertEquals(
                "Product type cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenProductTypeIsUnknown() {
        UnsupportedProductException exception = assertThrows(
                UnsupportedProductException.class,
                () -> resolver.resolve(ProductType.UNKNOWN)
        );

        assertEquals(
                "Unsupported product type: UNKNOWN",
                exception.getMessage()
        );
    }
}