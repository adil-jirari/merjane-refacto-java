package com.nimbleways.springboilerplate.domain.strategy;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.exception.UnsupportedProductException;

import java.util.Map;

public class ProductProcessingStrategyResolverImpl implements ProductProcessingStrategyResolver {

    private static final Map<ProductType, ProductProcessingStrategy> STRATEGIES = Map.of(
        ProductType.SEASONAL, new SeasonalProductProcessingStrategy(),
        ProductType.EXPIRABLE, new ExpirableProductProcessingStrategy(),
        ProductType.NORMAL, new NormalProductProcessingStrategy()
    );

    @Override
    public ProductProcessingStrategy resolve(ProductType productType) {
        if (productType == null) {
            throw new UnsupportedProductException("Product type cannot be null");
        }
        ProductProcessingStrategy productProcessingStrategy = STRATEGIES.get(productType);
        if (productProcessingStrategy == null) {
            throw new UnsupportedProductException("Unsupported product type: " + productType);
        }
        return productProcessingStrategy;
    }
}
