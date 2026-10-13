package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.event.ProductDelayedEvent;
import com.nimbleways.springboilerplate.domain.event.ProductExpiredEvent;
import com.nimbleways.springboilerplate.domain.event.ProductOutOfStockEvent;
import com.nimbleways.springboilerplate.domain.event.ProductProcessedEvent;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductDelayNotificationMessagePublisher;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductExpirationNotificationMessagePublisher;
import com.nimbleways.springboilerplate.service.ports.output.notification.ProductOutOfStockNotificationMessagePublisher;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ProductEventPublisherImplTest {

    @Mock
    private ProductDelayNotificationMessagePublisher
            productDelayNotificationMessagePublisher;

    @Mock
    private ProductExpirationNotificationMessagePublisher
            productExpirationNotificationMessagePublisher;

    @Mock
    private ProductOutOfStockNotificationMessagePublisher
            productOutOfStockNotificationMessagePublisher;

    @InjectMocks
    private ProductEventPublisherImpl productEventPublisher;

    @Mock
    private ProductDelayedEvent productDelayedEvent;

    @Mock
    private ProductExpiredEvent productExpiredEvent;

    @Mock
    private ProductOutOfStockEvent productOutOfStockEvent;

    @Mock
    private ProductProcessedEvent productProcessedEvent;

    @Test
    void shouldPublishDelayedEvent() {
        productEventPublisher.publish(productDelayedEvent);

        verify(productDelayNotificationMessagePublisher)
                .publish(productDelayedEvent);

        verifyNoInteractions(
                productExpirationNotificationMessagePublisher,
                productOutOfStockNotificationMessagePublisher
        );
    }

    @Test
    void shouldPublishExpiredEvent() {
        productEventPublisher.publish(productExpiredEvent);

        verify(productExpirationNotificationMessagePublisher)
                .publish(productExpiredEvent);

        verifyNoInteractions(
                productDelayNotificationMessagePublisher,
                productOutOfStockNotificationMessagePublisher
        );
    }

    @Test
    void shouldPublishOutOfStockEvent() {
        productEventPublisher.publish(productOutOfStockEvent);

        verify(productOutOfStockNotificationMessagePublisher)
                .publish(productOutOfStockEvent);

        verifyNoInteractions(
                productDelayNotificationMessagePublisher,
                productExpirationNotificationMessagePublisher
        );
    }

    @Test
    void shouldDoNothingForUnknownEvent() {
        productEventPublisher.publish(productProcessedEvent);

        verifyNoInteractions(
                productDelayNotificationMessagePublisher,
                productExpirationNotificationMessagePublisher,
                productOutOfStockNotificationMessagePublisher
        );
    }
}