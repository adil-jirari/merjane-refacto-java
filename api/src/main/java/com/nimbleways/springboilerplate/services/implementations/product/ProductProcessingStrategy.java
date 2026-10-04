package com.nimbleways.springboilerplate.services.implementations.product;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductProcessingStrategy {

    boolean supports(Product product);

    void process(Product product);
}