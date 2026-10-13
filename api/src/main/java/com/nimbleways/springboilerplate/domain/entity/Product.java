package com.nimbleways.springboilerplate.domain.entity;

import com.nimbleways.springboilerplate.common.domain.valueobject.ProductType;
import com.nimbleways.springboilerplate.domain.exception.OrderDomainException;
import com.nimbleways.springboilerplate.domain.valueobject.ProductId;

import java.time.LocalDate;

public class Product extends BaseEntity<ProductId> {

    private final Integer leadTime;
    private Integer available;
    private final ProductType type;
    private final String name;
    private final LocalDate expiryDate;
    private final LocalDate seasonStartDate;
    private final LocalDate seasonEndDate;

    public void validateProduct() {
        if(this.getId() == null || this.getId().getValue() == null) {
            throw new OrderDomainException("Product Id cannot be null or empty");
        }
        if (this.type == null) {
            throw new OrderDomainException("Product type cannot be null");
        }
    }

    public boolean hasAvailableStock() {
        return this.available > 0;
    }

    public boolean hasPositiveLeadTime() {
        return this.leadTime > 0;
    }

    public boolean isNotExpired(LocalDate today) {
        return this.expiryDate.isAfter(today);
    }

    public boolean isCurrentlyInSeason(LocalDate today) {
        return today.isAfter(this.seasonStartDate) && today.isBefore(this.seasonEndDate);
    }

    public boolean isRestockAfterSeasonEnd(LocalDate today) {
        return today.plusDays(this.leadTime).isAfter(this.seasonEndDate);
    }

    public boolean isBeforeSeasonStart(LocalDate today) {
        return this.seasonStartDate.isAfter(today);
    }

    public void decreaseAvailableStock() {
        this.available = this.available - 1;
    }

    public void markAsUnavailable() {
        this.available = 0;
    }

    private Product(Builder builder) {
        super.setId(builder.productId);
        this.name = builder.name;
        this.leadTime = builder.leadTime;
        this.available = builder.available;
        this.type = builder.type;
        this.expiryDate = builder.expiryDate;
        this.seasonStartDate = builder.seasonStartDate;
        this.seasonEndDate = builder.seasonEndDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProductId productId;
        private Integer leadTime;
        private Integer available;
        private ProductType type;
        private String name;
        private LocalDate expiryDate;
        private LocalDate seasonStartDate;
        private LocalDate seasonEndDate;

        private Builder() {
        }

        public Builder id(ProductId id) {
            productId = id;
            return this;
        }

        public Builder leadTime(Integer leadTime) {
            this.leadTime = leadTime;
            return this;
        }

        public Builder available(Integer available) {
            this.available = available;
            return this;
        }

        public Builder type(ProductType type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder seasonStartDate(LocalDate seasonStartDate) {
            this.seasonStartDate = seasonStartDate;
            return this;
        }

        public Builder seasonEndDate(LocalDate seasonEndDate) {
            this.seasonEndDate = seasonEndDate;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public ProductType getType() {
        return type;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public Integer getAvailable() {
        return available;
    }

    public String getName() {
        return name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDate getSeasonStartDate() {
        return seasonStartDate;
    }

    public LocalDate getSeasonEndDate() {
        return seasonEndDate;
    }
}
