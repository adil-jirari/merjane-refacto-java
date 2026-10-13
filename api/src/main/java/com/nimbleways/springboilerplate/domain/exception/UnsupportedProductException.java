package com.nimbleways.springboilerplate.domain.exception;

public class UnsupportedProductException extends RuntimeException {

    public UnsupportedProductException(String message) {
        super(message);
    }

    public UnsupportedProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
