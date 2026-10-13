package com.nimbleways.springboilerplate.infrastructure.persistence.repository;

import com.nimbleways.springboilerplate.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
}
