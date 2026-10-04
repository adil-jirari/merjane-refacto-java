package com.nimbleways.springboilerplate.services.implementations.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
class ExpirableProductProcessingStrategyTest {

    private static final String PRODUCT_NAME = "Milk";

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    private ExpirableProductProcessingStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new ExpirableProductProcessingStrategy(productRepository, notificationService);
    }

    @Test
    void shouldSupportExpirableProduct() {
        assertTrue(strategy.supports(product("EXPIRABLE", 1, 3)));
    }

    @Test
    void shouldNotSupportOtherProductTypes() {
        assertFalse(strategy.supports(product("NORMAL", 1, 3)));
    }

    @Test
    void shouldDecreaseAvailableStockWhenStockIsAvailableAndProductIsNotExpired() {
        Product product = product("EXPIRABLE", 4, 3);
        product.setExpiryDate(LocalDate.now().plusDays(2));

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(3, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void shouldMarkAsUnavailableWhenProductIsExpired() {
        LocalDate expiryDate = LocalDate.now().minusDays(1);
        Product product = product("EXPIRABLE", 4, 3);
        product.setExpiryDate(expiryDate);

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(notificationService).sendExpirationNotification(PRODUCT_NAME, expiryDate);
        verify(productRepository).save(product);
    }

    @Test
    void shouldMarkAsUnavailableWhenProductIsOutOfStock() {
        LocalDate expiryDate = LocalDate.now().plusDays(2);
        Product product = product("EXPIRABLE", 0, 3);
        product.setExpiryDate(expiryDate);

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(notificationService).sendExpirationNotification(PRODUCT_NAME, expiryDate);
        verify(productRepository).save(product);
    }

    private Product product(String type, int available, int leadTime) {
        return new Product(null, leadTime, available, type, PRODUCT_NAME, null, null, null);
    }
}