package com.nimbleways.springboilerplate.domain.entity;

import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.valueobject.OrderId;

import java.util.Set;
import java.util.UUID;

public class Order extends AgregateRoot<OrderId> {

    private final Set<Product> products;

    private Order(Builder builder) {
        this.setId(builder.id);
        this.products = builder.products;
    }

    public void validate() {
        validateId();
        validateProducts();
    }

    private void validateId() {
        if (getId() == null || getId().getValue() == null) {
            throw new OrderDomainException("Order Id cannot be null or empty");
        }
    }

    public void validateProducts() {
        if(products == null || products.isEmpty()) {
            throw new OrderDomainException("Order products cannot be null or empty");
        }
        for (Product product : products) {
            product.validateProduct();
        }
    }

    public Set<Product> getProducts() {
        return products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OrderId id;
        private Set<Product> products;

        public Builder id(OrderId id) {
            this.id = id;
            return this;
        }

        public Builder products(Set<Product> products) {
            this.products = products;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}