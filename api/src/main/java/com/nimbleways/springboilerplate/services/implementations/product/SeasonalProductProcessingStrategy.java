package com.nimbleways.springboilerplate.services.implementations.product;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;

import org.springframework.stereotype.Component;

@Component
public class SeasonalProductProcessingStrategy implements ProductProcessingStrategy {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public SeasonalProductProcessingStrategy(
            ProductRepository productRepository,
            NotificationService notificationService
    ) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public boolean supports(Product product) {
        return ProductType.SEASONAL.name().equals(product.getType());
    }

    @Override
    public void process(Product product) {
        if (isCurrentlyInSeason(product) && hasAvailableStock(product)) {
            decreaseAvailableStock(product);
        } else if (isRestockAfterSeasonEnd(product)) {
            markAsUnavailable(product);
        } else if (isBeforeSeasonStart(product)) {
            notifyUnavailable(product);
        } else {
            notifyDelay(product);
        }
    }

    private boolean isCurrentlyInSeason(Product product) {
        LocalDate today = LocalDate.now();
        return today.isAfter(product.getSeasonStartDate()) && today.isBefore(product.getSeasonEndDate());
    }

    private boolean hasAvailableStock(Product product) {
        return product.getAvailable() > 0;
    }

    private boolean isRestockAfterSeasonEnd(Product product) {
        return LocalDate.now().plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate());
    }

    private boolean isBeforeSeasonStart(Product product) {
        return product.getSeasonStartDate().isAfter(LocalDate.now());
    }

    private void decreaseAvailableStock(Product product) {
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    private void markAsUnavailable(Product product) {
        notificationService.sendOutOfStockNotification(product.getName());
        product.setAvailable(0);
        productRepository.save(product);
    }

    private void notifyUnavailable(Product product) {
        notificationService.sendOutOfStockNotification(product.getName());
        productRepository.save(product);
    }

    private void notifyDelay(Product product) {
        productRepository.save(product);
        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}