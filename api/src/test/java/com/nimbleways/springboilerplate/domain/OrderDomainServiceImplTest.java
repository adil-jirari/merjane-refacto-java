package com.nimbleways.springboilerplate.domain;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.domain.strategy.ProductProcessingStrategy;
import com.nimbleways.springboilerplate.domain.strategy.ProductProcessingStrategyResolver;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class OrderDomainServiceImplTest {

    @Mock
    private ProductProcessingStrategyResolver strategyResolver;

    @Mock
    private ProductProcessingStrategy strategy;

    @Mock
    private Order order;

    @Mock
    private Product product1;

    @Mock
    private Product product2;

    @Mock
    private ProductProcessedEvent event1;

    @Mock
    private ProductProcessedEvent event2;

    @InjectMocks
    private OrderDomainServiceImpl orderDomainService;

    @Test
    void shouldValidateOrderAndProcessAllProducts() {
        when(product1.getType()).thenReturn(ProductType.NORMAL);
        when(product2.getType()).thenReturn(ProductType.EXPIRABLE);

        when(strategyResolver.resolve(ProductType.NORMAL))
                .thenReturn(strategy);

        when(strategyResolver.resolve(ProductType.EXPIRABLE))
                .thenReturn(strategy);

        when(strategy.process(product1)).thenReturn(event1);

        when(strategy.process(product2)).thenReturn(event2);


        when(order.getProducts())
                .thenReturn(Set.of(product1, product2));

        List<ProductProcessedEvent> result =
                orderDomainService.validateAndProcessOrder(order);

        verify(order).validate();

        verify(strategyResolver)
                .resolve(ProductType.NORMAL);

        verify(strategyResolver)
                .resolve(ProductType.EXPIRABLE);

        verify(strategy).process(product1);
        verify(strategy).process(product2);

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenOrderContainsNoProducts() {
        when(order.getProducts()).thenReturn(Set.of());

        List<ProductProcessedEvent> result =
                orderDomainService.validateAndProcessOrder(order);

        verify(order).validate();

        assertEquals(0, result.size());

        verifyNoInteractions(strategyResolver);
    }
}