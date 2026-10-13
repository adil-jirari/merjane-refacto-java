package com.nimbleways.springboilerplate.infrastructure.application.exception.handler;

import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class OrderGlobalExceptionHandler {

    private static final String ORDER_DOMAIN_EXCEPTION = "Order Domain exception: {}";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Global exception: {}", exception.getMessage(), exception);
        return getErrorDTO(exception, status);
    }

    @ExceptionHandler(OrderDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(OrderDomainException orderDomainException) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error(ORDER_DOMAIN_EXCEPTION, orderDomainException.getMessage(), orderDomainException);
        return getErrorDTO(orderDomainException, status);
    }

    @ExceptionHandler(UnsupportedProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(UnsupportedProductException unsupportedProductException) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error(ORDER_DOMAIN_EXCEPTION, unsupportedProductException.getMessage(), unsupportedProductException);
        return getErrorDTO(unsupportedProductException, status);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(OrderNotFoundException orderNotFoundException) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.error(ORDER_DOMAIN_EXCEPTION, orderNotFoundException.getMessage(), orderNotFoundException);
        return getErrorDTO(orderNotFoundException, status);
    }

    private ErrorDTO getErrorDTO(Exception exception, HttpStatus status) {
        return ErrorDTO.builder()
                .code(status.getReasonPhrase())
                .message(exception.getMessage())
                .build();
    }

}
