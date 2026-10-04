package com.nimbleways.springboilerplate.services.implementations.product;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;

import org.springframework.stereotype.Component;

@Component
public class ExpirableProductProcessingStrategy implements ProductProcessingStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public ExpirableProductProcessingStrategy(
            ProductRepository productRepository,
            NotificationService notificationService
    ) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public boolean supports(Product product) {
        return ProductType.EXPIRABLE.name().equals(product.getType());
    }

    @Override
    public void process(Product product) {
        if (hasAvailableStock(product) && isNotExpired(product)) {
            decreaseAvailableStock(product);
        } else {
            markAsUnavailable(product);
        }
    }

    private boolean hasAvailableStock(Product product) {
        return product.getAvailable() > 0;
    }

    private boolean isNotExpired(Product product) {
        return product.getExpiryDate().isAfter(LocalDate.now());
    }

    private void decreaseAvailableStock(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    private void markAsUnavailable(Product product) {
        notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
        product.setAvailable(0);
        productRepository.save(product);
    }
}