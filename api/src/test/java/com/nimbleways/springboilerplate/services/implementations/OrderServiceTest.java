package com.nimbleways.springboilerplate.services.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productService);
    }

    @Test
    void shouldProcessEveryProductInOrderAndReturnOrderId() {
        Product firstProduct = new Product();
        Product secondProduct = new Product();

        Order order = new Order();
        order.setId(42L);
        order.setItems(Set.of(firstProduct, secondProduct));

        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));

        ProcessOrderResponse response = orderService.processOrder(42L);

        assertEquals(42L, response.id());
        verify(productService).process(firstProduct);
        verify(productService).process(secondProduct);
    }
}