package com.nimbleways.springboilerplate.infrastructure.application;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.ProductEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.OrderJpaRepository;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.ProductJpaRepository;
import com.nimbleways.springboilerplate.service.ports.output.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void processOrderShouldReturn() throws Exception {
        List<ProductEntity> allProducts = createProducts();
        Set<ProductEntity> orderItems = new HashSet<>(allProducts);
        OrderEntity order = createOrder(orderItems);
        productJpaRepository.saveAll(allProducts);
        order = orderJpaRepository.save(order);
        mockMvc.perform(post("/orders/{orderId}/processOrder", order.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getId().toString()));
        Optional<Order> resultOrder = orderRepository.findById(new OrderId(order.getId()));
        assertTrue(resultOrder.isPresent());
        assertNotNull(resultOrder.get().getId());
        assertNotNull(resultOrder.get().getProducts());
        assertEquals(resultOrder.get().getId().getValue(), order.getId());
    }

    @Test
    void processOrderShouldReturnNotFoundWhenOrderDoesNotExist()
            throws Exception {

        UUID orderId = UUID.randomUUID();

        mockMvc.perform(
                        post("/orders/{orderId}/processOrder", orderId)
                                .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Order not found: " + orderId));
    }

    private static OrderEntity createOrder(Set<ProductEntity> products) {
        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());
        order.setItems(products);
        return order;
    }

    private static List<ProductEntity> createProducts() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity(UUID.randomUUID(), 15, 30, ProductType.NORMAL, "USB Cable", null, null, null));
        products.add(new ProductEntity(UUID.randomUUID(), 10, 0, ProductType.NORMAL, "USB Dongle", null, null, null));
        products.add(new ProductEntity(UUID.randomUUID(), 15, 30, ProductType.EXPIRABLE, "Butter", LocalDate.now().plusDays(26), null, null));
        products.add(new ProductEntity(UUID.randomUUID(), 90, 6, ProductType.EXPIRABLE, "Milk", LocalDate.now().minusDays(2), null, null));
        products.add(new ProductEntity(UUID.randomUUID(), 15, 30, ProductType.SEASONAL, "Watermelon", null, LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(58)));
        products.add(new ProductEntity(UUID.randomUUID(), 15, 30, ProductType.SEASONAL, "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240)));
        return products;
    }


}