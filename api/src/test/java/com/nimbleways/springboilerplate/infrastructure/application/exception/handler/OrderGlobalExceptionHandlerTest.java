package com.nimbleways.springboilerplate.infrastructure.application.exception.handler;

import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class OrderGlobalExceptionHandlerTest {

    private final OrderGlobalExceptionHandler handler =
            new OrderGlobalExceptionHandler();

    @Test
    void shouldHandleGenericException() {
        Exception exception =
                new RuntimeException("Unexpected error");

        ErrorDTO result =
                handler.handleException(exception);

        assertNotNull(result);
        assertEquals(
                "Internal Server Error",
                result.getCode()
        );
        assertEquals(
                "Unexpected error",
                result.getMessage()
        );
    }

    @Test
    void shouldHandleOrderDomainException() {
        OrderDomainException exception =
                new OrderDomainException("Business rule violated");

        ErrorDTO result =
                handler.handleException(exception);

        assertNotNull(result);
        assertEquals(
                "Bad Request",
                result.getCode()
        );
        assertEquals(
                "Business rule violated",
                result.getMessage()
        );
    }

    @Test
    void shouldHandleUnsupportedProductException() {
        UnsupportedProductException exception =
                new UnsupportedProductException(
                        "Unsupported product type");

        ErrorDTO result =
                handler.handleException(exception);

        assertNotNull(result);
        assertEquals(
                "Bad Request",
                result.getCode()
        );
        assertEquals(
                "Unsupported product type",
                result.getMessage()
        );
    }

    @Test
    void shouldHandleOrderNotFoundException() {
        OrderNotFoundException exception =
                new OrderNotFoundException(
                        "Order not found");

        ErrorDTO result =
                handler.handleException(exception);

        assertNotNull(result);
        assertEquals(
                "Not Found",
                result.getCode()
        );
        assertEquals(
                "Order not found",
                result.getMessage()
        );
    }
}