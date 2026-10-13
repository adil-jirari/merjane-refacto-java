package com.nimbleways.springboilerplate.infrastructure.persistence.mapper;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.ProductEntity;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class OrderDataAccessMapperTest {

    private final OrderDataAccessMapper mapper =
            new OrderDataAccessMapper();

    @Test
    void shouldMapProductEntityToProduct() {
        UUID id = UUID.randomUUID();

        ProductEntity entity = ProductEntity.builder()
                .id(id)
                .name("Milk")
                .type(ProductType.EXPIRABLE)
                .available(10)
                .leadTime(5)
                .expiryDate(LocalDate.now().plusDays(10))
                .build();

        Product result = mapper.productEntityToProduct(entity);

        assertEquals(id, result.getId().getValue());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getType(), result.getType());
        assertEquals(entity.getAvailable(), result.getAvailable());
        assertEquals(entity.getLeadTime(), result.getLeadTime());
        assertEquals(entity.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    void shouldMapProductToProductEntity() {
        UUID id = UUID.randomUUID();

        Product product = Product.builder()
                .id(new ProductId(id))
                .name("Milk")
                .type(ProductType.EXPIRABLE)
                .available(10)
                .leadTime(5)
                .expiryDate(LocalDate.now().plusDays(10))
                .build();

        ProductEntity result = mapper.productToProductEntity(product);

        assertEquals(id, result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getType(), result.getType());
        assertEquals(product.getAvailable(), result.getAvailable());
        assertEquals(product.getLeadTime(), result.getLeadTime());
        assertEquals(product.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    void shouldMapOrderEntityToOrder() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        ProductEntity productEntity = ProductEntity.builder()
                .id(productId)
                .name("USB Cable")
                .type(ProductType.NORMAL)
                .available(10)
                .leadTime(2)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .items(Set.of(productEntity))
                .build();

        Order result = mapper.orderEntityToOrder(orderEntity);

        assertEquals(orderId, result.getId().getValue());
        assertEquals(1, result.getProducts().size());

        Product product = result.getProducts().iterator().next();

        assertEquals(productId, product.getId().getValue());
        assertEquals("USB Cable", product.getName());
        assertEquals(ProductType.NORMAL, product.getType());
    }

    @Test
    void shouldMapOrderToOrderEntity() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .id(new ProductId(productId))
                .name("USB Cable")
                .type(ProductType.NORMAL)
                .available(10)
                .leadTime(2)
                .build();

        Order order = Order.builder()
                .id(new OrderId(orderId))
                .products(Set.of(product))
                .build();

        OrderEntity result = mapper.orderToOrderEntity(order);

        assertEquals(orderId, result.getId());
        assertEquals(1, result.getItems().size());

        ProductEntity productEntity =
                result.getItems().iterator().next();

        assertEquals(productId, productEntity.getId());
        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getType(), productEntity.getType());
    }
}

