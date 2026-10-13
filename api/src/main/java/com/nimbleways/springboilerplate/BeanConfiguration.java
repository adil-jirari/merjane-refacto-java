package com.nimbleways.springboilerplate;

import com.nimbleways.springboilerplate.domain.OrderDomainService;
import com.nimbleways.springboilerplate.domain.OrderDomainServiceImpl;
import com.nimbleways.springboilerplate.domain.strategy.ProductProcessingStrategyResolver;
import com.nimbleways.springboilerplate.domain.strategy.ProductProcessingStrategyResolverImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductProcessingStrategyResolver productProcessingStrategyResolver() {
        return new ProductProcessingStrategyResolverImpl();
    }

    @Bean
    public OrderDomainService orderDomainService(ProductProcessingStrategyResolver productProcessingStrategyResolver) {
        return new OrderDomainServiceImpl(productProcessingStrategyResolver);
    }
}
