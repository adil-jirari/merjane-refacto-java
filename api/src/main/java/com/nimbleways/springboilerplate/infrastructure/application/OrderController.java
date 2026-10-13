package com.nimbleways.springboilerplate.infrastructure.application;

import com.nimbleways.springboilerplate.service.dto.request.OrderProcessCommand;
import com.nimbleways.springboilerplate.service.dto.response.OrderProcessResponse;
import com.nimbleways.springboilerplate.service.ports.input.service.OrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations related to order processing")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @Operation(summary = "Process an order", description = "Processes every product in an order and updates product availability.")
    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public OrderProcessResponse processOrder(@PathVariable UUID orderId) {
        return orderApplicationService.processOrder(OrderProcessCommand.builder().orderId(orderId).build());

    }
}
