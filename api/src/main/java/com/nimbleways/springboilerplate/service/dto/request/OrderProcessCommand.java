package com.nimbleways.springboilerplate.service.dto.request;

import com.nimbleways.springboilerplate.domain.valueobject.OrderId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderProcessCommand {

    @NotNull
    private final UUID orderId;
}
