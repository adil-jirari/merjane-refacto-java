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
class SeasonalProductProcessingStrategyTest {

    private static final String PRODUCT_NAME = "Watermelon";

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NotificationService notificationService;

    private SeasonalProductProcessingStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new SeasonalProductProcessingStrategy(productRepository, notificationService);
    }

    @Test
    void shouldSupportSeasonalProduct() {
        assertTrue(strategy.supports(product("SEASONAL", 1, 3)));
    }

    @Test
    void shouldNotSupportOtherProductTypes() {
        assertFalse(strategy.supports(product("NORMAL", 1, 3)));
    }

    @Test
    void shouldDecreaseAvailableStockWhenCurrentlyInSeasonAndStockIsAvailable() {
        LocalDate today = LocalDate.now();
        Product product = product("SEASONAL", 2, 3);
        product.setSeasonStartDate(today.minusDays(1));
        product.setSeasonEndDate(today.plusDays(10));

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(1, product.getAvailable());
        verify(productRepository).save(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    void shouldMarkAsUnavailableWhenRestockIsAfterSeasonEnd() {
        LocalDate today = LocalDate.now();
        Product product = product("SEASONAL", 0, 15);
        product.setSeasonStartDate(today.minusDays(10));
        product.setSeasonEndDate(today.plusDays(5));

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(notificationService).sendOutOfStockNotification(PRODUCT_NAME);
        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifyUnavailableWhenBeforeSeasonStart() {
        LocalDate today = LocalDate.now();
        Product product = product("SEASONAL", 0, 2);
        product.setSeasonStartDate(today.plusDays(5));
        product.setSeasonEndDate(today.plusDays(20));

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(notificationService).sendOutOfStockNotification(PRODUCT_NAME);
        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifyDelayWhenOutOfStockAndRestockIsBeforeSeasonEnd() {
        LocalDate today = LocalDate.now();
        Product product = product("SEASONAL", 0, 2);
        product.setSeasonStartDate(today.minusDays(5));
        product.setSeasonEndDate(today.plusDays(10));

        when(productRepository.save(product)).thenReturn(product);

        strategy.process(product);

        assertEquals(0, product.getAvailable());
        verify(notificationService).sendDelayNotification(2, PRODUCT_NAME);
        verify(productRepository).save(product);
    }

    private Product product(String type, int available, int leadTime) {
        return new Product(null, leadTime, available, type, PRODUCT_NAME, null, null, null);
    }
}