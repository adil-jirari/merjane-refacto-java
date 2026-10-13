package com.nimbleways.springboilerplate.infrastructure.notification;

import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductOutOfStockNotificationMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductOutOfStockNotificationMessagePublisherImpl implements ProductOutOfStockNotificationMessagePublisher {

    @Override
    public void publish(ProductProcessedEvent event) {
        log.info("Sending out of stock notification for product {}", event.getProduct().getName());
    }
}
