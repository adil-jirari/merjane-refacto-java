package com.nimbleways.springboilerplate.infrastructure.notification;

import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductExpirationNotificationMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductExpirationNotificationMessagePublisherImpl implements ProductExpirationNotificationMessagePublisher {

    @Override
    public void publish(ProductProcessedEvent event) {
        log.info("Sending expiration notification for product {}", event.getProduct().getName());
    }
}
