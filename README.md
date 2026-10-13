# Order Processing Refactoring

## Overview

This project has been refactored to improve maintainability, extensibility, testability, and separation of concerns by applying several software design principles and architectural patterns:

- Domain-Driven Design (DDD)
- Hexagonal Architecture (Ports & Adapters)
- Strategy Pattern
- Domain Events
- Repository Pattern
- Application Services
- Persistence Mapping Layer

The objective of this refactoring is to isolate business rules from infrastructure concerns and make the system easier to evolve as new product types or notification channels are introduced.

---

# Original Challenges

The initial implementation contained:

- Business rules mixed with technical concerns
- Product type conditionals spread across the codebase
- Tight coupling between processing logic and notification delivery
- Direct dependency on persistence implementation details
- Difficult extensibility when introducing new product types

Refactoring focused on improving:

- Separation of concerns
- Domain expressiveness
- Testability
- Extensibility
- Maintainability

---

# Architecture

```text
src/main/java

domain
├── entity
│   ├── Order
│   └── Product
│
├── event
│   ├── ProductProcessedEvent
│   ├── ProductDecreasedEvent
│   ├── ProductDelayedEvent
│   ├── ProductExpiredEvent
│   └── ProductOutOfStockEvent
│
├── strategy
│   ├── ProductProcessingStrategy
│   ├── NormalProductProcessingStrategy
│   ├── ExpirableProductProcessingStrategy
│   ├── SeasonalProductProcessingStrategy
│   ├── ProductProcessingStrategyResolver
│   └── ProductProcessingStrategyResolverImpl
│
├── exception
│
└── OrderDomainService

service
├── OrderApplicationService
├── ProductEventPublisher
│
├── dto
│
└── ports
    ├── input
    └── output
        ├── repository
        └── notification

infrastructure
├── application
│   └── controller
│
├── persistence
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── adapter
│
└── notification
```

---

# Domain-Driven Design

## Aggregate Root

The Order has been modeled as the Aggregate Root.

```text
Order
 └── Products
```

The application layer interacts with products through the order aggregate instead of manipulating products independently.

Responsibilities of the aggregate:

- Validate business invariants
- Provide access to products to process
- Protect domain consistency

---

## Rich Domain Model

Business behavior was moved from services into domain entities.

Examples:

```java
product.hasAvailableStock();
product.hasPositiveLeadTime();
product.isNotExpired(today);
product.isCurrentlyInSeason(today);
product.isBeforeSeasonStart(today);
product.isRestockAfterSeasonEnd(today);

product.decreaseAvailableStock();
product.markAsUnavailable();
```

This approach keeps business rules close to the business data.

---

# Strategy Pattern

Product-specific processing behavior is implemented using the Strategy Pattern.

## Strategy Interface

```java
public interface ProductProcessingStrategy {
    ProductProcessedEvent process(Product product);
}
```

---

## Normal Products

Implementation:

```java
NormalProductProcessingStrategy
```

Possible outcomes:

- ProductDecreasedEvent
- ProductDelayedEvent
- OrderDomainException

Example:

```java
if (product.hasAvailableStock()) {
    ...
}
```

---

## Expirable Products

Implementation:

```java
ExpirableProductProcessingStrategy
```

Possible outcomes:

- ProductDecreasedEvent
- ProductExpiredEvent

Example:

```java
if(product.hasAvailableStock() &&
   product.isNotExpired(today))
```

---

## Seasonal Products

Implementation:

```java
SeasonalProductProcessingStrategy
```

Possible outcomes:

- ProductDecreasedEvent
- ProductDelayedEvent
- ProductOutOfStockEvent

Example:

```java
if(product.isCurrentlyInSeason(today))
```

---

# Strategy Resolver

Strategy selection is centralized through a resolver.

```java
ProductProcessingStrategyResolver
```

Implementation:

```java
ProductProcessingStrategyResolverImpl
```

Usage:

```java
ProductProcessingStrategy strategy =
        strategyResolver.resolve(product.getType());
```

Benefits:

- Removes product-type conditionals
- Centralizes strategy retrieval
- Simplifies extension for new product types

---

# Domain Events

Domain processing produces events representing business outcomes.

Base event:

```java
ProductProcessedEvent
```

Specializations:

```java
ProductDecreasedEvent
ProductDelayedEvent
ProductExpiredEvent
ProductOutOfStockEvent
```

Benefits:

- Explicit business outcomes
- Decoupling from notification delivery
- Easier future event-driven evolution

---

# Domain Service

## OrderDomainService

The domain service orchestrates the processing of products inside an order.

Implementation:

```java
OrderDomainServiceImpl
```

Responsibilities:

- Validate the order
- Resolve processing strategy
- Process products
- Produce domain events

Example:

```java
List<ProductProcessedEvent> events =
        orderDomainService
                .validateAndProcessOrder(order);
```

---

# Application Layer

## OrderApplicationService

The application service coordinates the use case execution.

Implementation:

```java
OrderApplicationServiceImpl
```

Workflow:

```text
Load Aggregate
    ↓
Execute Domain Logic
    ↓
Persist Changes
    ↓
Publish Events
```

Example:

```java
Order order =
        orderRepository.findById(...);

List<ProductProcessedEvent> events =
        orderDomainService
                .validateAndProcessOrder(order);

productRepository.save(...);

productEventPublisher.publish(...);
```

The application service contains orchestration logic only.

Business rules remain inside the domain layer.

---

# Hexagonal Architecture

The project follows a Ports & Adapters approach.

## Input Ports

Expose application use cases.

```java
OrderApplicationService
```

---

## Output Ports

Repositories:

```java
OrderRepository
ProductRepository
```

Notification publishers:

```java
ProductDelayNotificationMessagePublisher

ProductExpirationNotificationMessagePublisher

ProductOutOfStockNotificationMessagePublisher
```

The core business layer depends only on abstractions.

---

# Persistence Layer

## Repository Adapters

Infrastructure-specific implementations:

```java
OrderRepositoryImpl
ProductRepositoryImpl
```

Responsibilities:

- JPA interaction
- Mapping between domain and persistence models

---

## Persistence Entities

JPA entities were introduced to isolate persistence concerns.

Examples:

```java
OrderEntity
ProductEntity
```

The domain model remains completely independent from JPA.

---

## Mapper Layer

Implementation:

```java
OrderDataAccessMapper
```

Responsibilities:

```text
Domain Model
      ↕
Persistence Model
```

Examples:

```java
OrderEntity ↔ Order

ProductEntity ↔ Product
```

Benefits:

- Domain model is persistence-agnostic
- No JPA annotations in domain entities
- Cleaner architecture boundaries

---

# Event Publication

A dedicated publisher abstraction has been introduced.

Interface:

```java
ProductEventPublisher
```

Implementation:

```java
ProductEventPublisherImpl
```

Responsibilities:

```text
ProductDelayedEvent
        ↓
Delay Notification

ProductExpiredEvent
        ↓
Expiration Notification

ProductOutOfStockEvent
        ↓
Out Of Stock Notification
```

This prevents the application service from knowing notification implementation details.

---

# Transaction Management

Order processing is transactional.

```java
@Transactional
public OrderProcessResponse processOrder(...)
```

Current flow:

```text
Load Order
    ↓
Process Products
    ↓
Persist Updates
    ↓
Publish Events
```

Products are persisted before event publication.

---

# Error Handling

Application and domain exceptions are translated into HTTP responses through a dedicated global exception handler.

| Exception | HTTP Status |
|-----------|-------------|
| OrderNotFoundException | 404 Not Found |
| UnsupportedProductException | 400 Bad Request |
| OrderDomainException | 400 Bad Request |
| Exception | 500 Internal Server Error |

# Testing Strategy

The project includes both unit and integration tests.

## Domain Tests

```text
NormalProductProcessingStrategyTest

ExpirableProductProcessingStrategyTest

SeasonalProductProcessingStrategyTest

ProductProcessingStrategyResolverImplTest

OrderDomainServiceImplTest
```

---

## Infrastructure Tests

```text
OrderRepositoryImplTest

ProductRepositoryImplTest

OrderDataAccessMapperTest
```

---

## Application Tests

```text
OrderApplicationServiceImplTest

ProductEventPublisherImplTest
```

---

## Integration Tests

```text
OrderControllerIntegrationTests
```

The integration tests validate the full processing workflow.

---

# Key Improvements

✅ Applied Domain-Driven Design

✅ Introduced Aggregate Root pattern

✅ Introduced Rich Domain Model

✅ Introduced Strategy Pattern

✅ Added Domain Events

✅ Added Domain Service

✅ Applied Hexagonal Architecture

✅ Introduced Repository Abstractions

✅ Added Persistence Mapping Layer

✅ Decoupled Notification Delivery

✅ Improved 