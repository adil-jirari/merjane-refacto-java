package com.nimbleways.springboilerplate.infrastructure.persistence.adapter;

import com.nimbleways.springboilerplate.domain.entity.Product;
import com.nimbleways.springboilerplate.infrastructure.persistence.entity.ProductEntity;
import com.nimbleways.springboilerplate.infrastructure.persistence.mapper.OrderDataAccessMapper;
import com.nimbleways.springboilerplate.infrastructure.persistence.repository.ProductJpaRepository;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ProductRepositoryImplTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private OrderDataAccessMapper orderDataAccessMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @Mock
    private Product product;

    @Mock
    private Product product2;

    @Mock
    private ProductEntity productEntity;

    @Mock
    private ProductEntity productEntity2;

    @Test
    void shouldSaveProduct() {
        when(orderDataAccessMapper.productToProductEntity(product))
                .thenReturn(productEntity);

        when(productJpaRepository.save(productEntity))
                .thenReturn(productEntity);

        when(orderDataAccessMapper.productEntityToProduct(productEntity))
                .thenReturn(product);

        Product result = productRepository.save(product);

        assertNotNull(result);
        assertEquals(product, result);

        verify(orderDataAccessMapper)
                .productToProductEntity(product);

        verify(productJpaRepository)
                .save(productEntity);

        verify(orderDataAccessMapper)
                .productEntityToProduct(productEntity);
    }

    @Test
    void shouldSaveAllProducts() {
        Set<Product> products = Set.of(product, product2);

        when(orderDataAccessMapper.productToProductEntity(product))
                .thenReturn(productEntity);

        when(orderDataAccessMapper.productToProductEntity(product2))
                .thenReturn(productEntity2);

        when(productJpaRepository.saveAll(anyList()))
                .thenReturn(List.of(productEntity, productEntity2));

        when(orderDataAccessMapper.productEntityToProduct(productEntity))
                .thenReturn(product);

        when(orderDataAccessMapper.productEntityToProduct(productEntity2))
                .thenReturn(product2);

        List<Product> result =
                productRepository.saveAll(products);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(product));
        assertTrue(result.contains(product2));

        verify(orderDataAccessMapper)
                .productToProductEntity(product);

        verify(orderDataAccessMapper)
                .productToProductEntity(product2);

        verify(productJpaRepository)
                .saveAll(anyList());

        verify(orderDataAccessMapper)
                .productEntityToProduct(productEntity);

        verify(orderDataAccessMapper)
                .productEntityToProduct(productEntity2);
    }
}