package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.event.ProductDelayedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductExpiredEvent;
import com.nimbleways.springboilerplate.domain.event.ProductOutOfStockEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.service.ports.input.service.ProductEventPublisher;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductDelayNotificationMessagePublisher;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductExpirationNotificationMessagePublisher;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductOutOfStockNotificationMessagePublisher;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisherImpl implements ProductEventPublisher {

    private final ProductDelayNotificationMessagePublisher productDelayNotificationMessagePublisher;
    private final ProductExpirationNotificationMessagePublisher productExpirationNotificationMessagePublisher;
    private final ProductOutOfStockNotificationMessagePublisher productOutOfStockNotificationMessagePublisher;

    public ProductEventPublisherImpl(ProductDelayNotificationMessagePublisher productDelayNotificationMessagePublisher,
                                     ProductExpirationNotificationMessagePublisher productExpirationNotificationMessagePublisher,
                                     ProductOutOfStockNotificationMessagePublisher productOutOfStockNotificationMessagePublisher) {
        this.productDelayNotificationMessagePublisher = productDelayNotificationMessagePublisher;
        this.productExpirationNotificationMessagePublisher = productExpirationNotificationMessagePublisher;
        this.productOutOfStockNotificationMessagePublisher = productOutOfStockNotificationMessagePublisher;
    }
    @Override
    public void publish(ProductProcessedEvent event) {
        if (event instanceof ProductDelayedEvent productDelayedEvent) {
            productDelayNotificationMessagePublisher.publish(productDelayedEvent);
        } else if (event instanceof ProductExpiredEvent productExpiredEvent) {
            productExpirationNotificationMessagePublisher.publish(productExpiredEvent);
        } else if (event instanceof ProductOutOfStockEvent productOutOfStockEvent) {
            productOutOfStockNotificationMessagePublisher.publish(productOutOfStockEvent);
        }
    }
}
