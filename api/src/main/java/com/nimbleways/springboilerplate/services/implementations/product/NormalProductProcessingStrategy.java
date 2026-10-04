package com.nimbleways.springboilerplate.services.implementations.product;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;

import org.springframework.stereotype.Component;

@Component
public class NormalProductProcessingStrategy implements ProductProcessingStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public NormalProductProcessingStrategy(
            ProductRepository productRepository,
            NotificationService notificationService
    ) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public boolean supports(Product product) {
        return ProductType.NORMAL.name().equals(product.getType());
    }

    @Override
    public void process(Product product) {
        if (hasAvailableStock(product)) {
            decreaseAvailableStock(product);
        } else if (hasPositiveLeadTime(product)) {
            notifyDelay(product);
        }
    }

    private boolean hasAvailableStock(Product product) {
        return product.getAvailable() > 0;
    }

    private boolean hasPositiveLeadTime(Product product) {
        return product.getLeadTime() > 0;
    }

    private void decreaseAvailableStock(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    private void notifyDelay(Product product) {
        productRepository.save(product);
        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}