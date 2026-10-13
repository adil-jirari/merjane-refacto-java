package com.nimbleways.springboilerplate.infrastructure.persistence.mapper;

import com.nimbleways.springboilerplate.domain.entity.Order;
import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.OrderEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper {

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .id(new OrderId(orderEntity.getId()))
                .products(productEntitiesToProducts(orderEntity.getItems()))
                .build();
    }

    public OrderEntity orderToOrderEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId().getValue())
                .items(order.getProducts().stream().map(this::productToProductEntity).collect(Collectors.toSet()))
                .build();
    }

    private Set<Product> productEntitiesToProducts(Set<ProductEntity> items) {
        return items.stream().map(this::productEntityToProduct).collect(Collectors.toSet());
    }

    public Product productEntityToProduct(ProductEntity product) {
        return Product.builder()
                .id(new ProductId(product.getId()))
                .name(product.getName())
                .type(product.getType())
                .leadTime(product.getLeadTime())
                .available(product.getAvailable())
                .expiryDate(product.getExpiryDate())
                .seasonStartDate(product.getSeasonStartDate())
                .seasonEndDate(product.getSeasonEndDate())
                .build();
    }

    public ProductEntity productToProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId().getValue())
                .type(product.getType())
                .name(product.getName())
                .seasonStartDate(product.getSeasonStartDate())
                .seasonEndDate(product.getSeasonEndDate())
                .available(product.getAvailable())
                .expiryDate(product.getExpiryDate())
                .leadTime(product.getLeadTime())
                .build();
    }
}
