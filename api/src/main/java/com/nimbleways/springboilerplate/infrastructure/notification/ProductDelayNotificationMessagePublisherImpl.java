package com.nimbleways.springboilerplate.infrastructure.notification;

import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductDelayNotificationMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductDelayNotificationMessagePublisherImpl implements ProductDelayNotificationMessagePublisher {

    @Override
    public void publish(ProductProcessedEvent event) {
        log.info("Sending delay notification for product {}", event.getProduct().getName());
    }
}
