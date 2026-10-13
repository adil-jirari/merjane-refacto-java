package com.nimbleways.springboilerplate.service.ports.output.repository;

import com.nimbleways.springboilerplate.domain.entity.Product;

public interface ProductRepository {

    Product save(Product product);

}
