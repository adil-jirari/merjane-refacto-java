package com.nimbleways.springboilerplate.services.implementations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.implementations.product.ProductProcessingStrategy;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
class ProductServiceTest {

    @Mock
    private ProductProcessingStrategy matchingStrategy;

    @Mock
    private ProductProcessingStrategy otherStrategy;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(List.of(otherStrategy, matchingStrategy));
    }

    @Test
    void shouldProcessProductWithMatchingStrategy() {
        Product product = new Product();

        when(otherStrategy.supports(product)).thenReturn(false);
        when(matchingStrategy.supports(product)).thenReturn(true);

        productService.process(product);

        verify(matchingStrategy).process(product);
        verify(otherStrategy, never()).process(product);
    }

    @Test
    void shouldDoNothingWhenNoStrategyMatchesProduct() {
        Product product = new Product();

        when(otherStrategy.supports(product)).thenReturn(false);
        when(matchingStrategy.supports(product)).thenReturn(false);

        productService.process(product);

        verify(otherStrategy, never()).process(product);
        verify(matchingStrategy, never()).process(product);
    }
}