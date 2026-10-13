package com.nimbleways.springboilerplate.common.domain.event;

public interface DomainEventPublisher <T extends DomainEvent<?>> {

    void publish(T event);
}
