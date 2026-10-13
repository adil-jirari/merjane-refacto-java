package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;

public interface ProductProcessingStrategyResolver {

    ProductProcessingStrategy resolve(ProductType productType);
}
