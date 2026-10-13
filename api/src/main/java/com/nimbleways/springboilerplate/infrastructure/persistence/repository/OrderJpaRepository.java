package com.nimbleways.springboilerplate.infrastructure.persistence.repository;

import com.nimbleways.springboilerplate.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("""
        SELECT distinct o
        FROM OrderEntity o
        LEFT JOIN FETCH o.items
        WHERE o.id = :orderId
    """)
    Optional<OrderEntity> findAggregateById(UUID orderId);

}
