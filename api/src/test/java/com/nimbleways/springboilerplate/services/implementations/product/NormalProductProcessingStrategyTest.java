package com.nimbleways.springboilerplate.services.implementations.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
class NormalProductProcessingStrategyTest {

    private static final String PRODUCT_NAME = "USB Cable";

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    private NormalProductProcessingStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new NormalProductProcessingStrategy(productRepository, notificationService);
    }

    @Test
    void shouldSupportNormalProduct() {
        assertTrue(strategy.supports(product("NORMAL", 1, 3)));
    }

    @Test
    void shouldNotSupportOtherProductTypes() {
        assertFalse(strategy.supports(product("SEASONAL", 1, 3)));
    }

    @Test
    void shouldDecreaseAvailableStockWhenStockIsAvailable() {
        Product product = product("NORMAL", 5, 3);

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(4, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void shouldNotifyDelayWhenOutOfStockAndLeadTimeIsPositive() {
        Product product = product("NORMAL", 0, 3);

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(productRepository).save(product);
        verify(notificationService).sendDelayNotification(3, PRODUCT_NAME);
    }

    @Test
    void shouldDoNothingWhenOutOfStockAndLeadTimeIsNotPositive() {
        Product product = product("NORMAL", 0, 0);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verifyNoInteractions(productRepository);
        verifyNoInteractions(notificationService);
    }

    private Product product(String type, int available, int leadTime) {
        return new Product(null, leadTime, available, type, PRODUCT_NAME, null, null, null);
    }
}