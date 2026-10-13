package com.nimbleways.springboilerplate.service.ports.output.notification;

import com.nimbleways.springboilerplate.common.domain.event.DomainEventPublisher;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;

public interface ProductExpirationNotificationMessagePublisher extends DomainEventPublisher<ProductProcessedEvent> {
}
