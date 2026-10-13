package com.nimbleways.springboilerplate.infrastructure.persistence.adapter;

import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.infrastructure.persistence.mapper.OrderDataAccessMapper;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.ProductJpaRepository;
import com.nimbleways.springboilerplate.service.ports.output.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.productJpaRepository = productJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Product save(Product product) {
        return orderDataAccessMapper.productEntityToProduct(
                productJpaRepository.save(orderDataAccessMapper.productToProductEntity(product)));
    }

    public List<Product> saveAll(Set<Product> products) {
        return productJpaRepository.saveAll(
                products.stream().map(orderDataAccessMapper::productToProductEntity).toList())
                .stream().map(orderDataAccessMapper::productEntityToProduct).toList();
    }
}
