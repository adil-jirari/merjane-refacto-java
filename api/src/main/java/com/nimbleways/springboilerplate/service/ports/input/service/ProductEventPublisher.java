package com.nimbleways.springboilerplate.service.ports.input.service;

import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;

public interface ProductEventPublisher {

    void publish(ProductProcessedEvent event);
}
