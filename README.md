### Consignes: 
* Ignorez les migrations BDD
* Ne pas modifier les classes qui ont un commentaire: `// WARN: Should not be changed during the exercise
`
* Pour lancer les tests (depuis le sous-répertoire `api`) :
  * unitaires: `mvnw test`
  * integration: `mvnw integration-test`
  * tous: `mvnw verify`

# Merjane Refactoring Exercise

This project is a refactoring exercise for the Merjane inventory and order processing application.

The goal of the exercise is to improve the existing codebase while ensuring that no business regression is introduced.

## Business Context

Merjane tracks product availability during order processing.

Each product has:

- `available`: number of units available in stock
- `leadTime`: number of days required for restocking
- `type`: product category

The supported product types are:

- `NORMAL`
- `SEASONAL`
- `EXPIRABLE`

### NORMAL products

Normal products do not have specific constraints.

When stock is available, the product availability is decreased after an order is processed.

When the product is out of stock, a delay notification is sent if a positive lead time exists.

### SEASONAL products

Seasonal products are available only during a specific season.

When stock is available and the product is currently in season, the product availability is decreased.

When the product is out of stock:

- if the restocking date is after the season end date, the product is considered unavailable;
- if the product is not yet in season, the product is considered unavailable;
- otherwise, a delay notification is sent.

### EXPIRABLE products

Expirable products can be sold only before their expiration date.

When stock is available and the product is not expired, the product availability is decreased.

When the product is expired or unavailable, it is marked as unavailable and an expiration notification is sent.

---

## Refactoring Summary

The original implementation contained business logic inside the controller and mixed several responsibilities in the same service.

The refactoring focused on:

- improving separation of concerns;
- moving business logic out of the controller;
- applying the Strategy pattern for product-specific rules;
- increasing test coverage around business behavior;
- preserving the existing behavior as much as possible.

---

## Architecture

### Controller layer

The controller is responsible only for HTTP concerns.

It receives the request and delegates order processing to `OrderService`.

text controllers └── MyController.java

### Service layer

`OrderService` orchestrates order processing.

It retrieves the order and delegates each product processing to `ProductService`.

text services └── implementations ├── OrderService.java ├── ProductService.java └── NotificationService.java

### Product processing strategies

`ProductService` acts as a router and delegates product processing to the matching strategy.

Each product type has its own strategy:

text services └── implementations └── product ├── ProductProcessingStrategy.java ├── NormalProductProcessingStrategy.java ├── SeasonalProductProcessingStrategy.java └── ExpirableProductProcessingStrategy.java

This design follows the Open/Closed Principle:

- adding a new product type requires adding a new strategy;
- existing strategies do not need to be modified;
- `ProductService` remains simple and focused.

---

## Main Design Decisions

### Strategy Pattern

The Strategy pattern was introduced to isolate product-specific rules.

Before refactoring, the product processing logic was implemented using conditional branches based on product type.

After refactoring:

- `NormalProductProcessingStrategy` handles `NORMAL` products;
- `SeasonalProductProcessingStrategy` handles `SEASONAL` products;
- `ExpirableProductProcessingStrategy` handles `EXPIRABLE` products.

This makes the code easier to read, test, and extend.

### OrderService Extraction

Order processing orchestration was extracted from the controller into `OrderService`.

This keeps the controller thin and improves the separation between HTTP handling and business logic.

### ProductType Enum

A `ProductType` enum was introduced to avoid spreading raw string values across the codebase.

This improves readability and reduces the risk of typos in product type handling.

### Repository Identifier Type

`ProductRepository` was aligned with the `Product` entity identifier type.

Since `Product.id` is a `Long`, the repository uses `Long` as its identifier type.

---

## Tests

The project contains unit and integration tests.

### Unit tests

Unit tests cover the business logic in isolation:

text services └── implementations ├── OrderServiceTest.java ├── ProductServiceTest.java └── product ├── NormalProductProcessingStrategyTest.java ├── SeasonalProductProcessingStrategyTest.java └── ExpirableProductProcessingStrategyTest.java

Covered cases include:

- normal product with available stock;
- normal product out of stock with lead time;
- normal product out of stock without lead time;
- seasonal product currently in season;
- seasonal product before season start;
- seasonal product with restocking after season end;
- seasonal product with valid restocking delay;
- expirable product not expired;
- expirable product expired;
- expirable product out of stock;
- product routing through `ProductService`;
- order orchestration through `OrderService`.

### Integration tests

Integration tests validate the application context and the order processing endpoint.

text controllers └── MyControllerIntegrationTests.java
ApplicationIntegrationTests.java

---

## Running the Project

All commands must be executed from the `api` directory.
bash cd api

### Run unit tests
bash ./mvnw test

### Run integration tests
bash ./mvnw integration-test

### Run all checks
bash ./mvnw verify

The `verify` command runs:

- unit tests;
- integration tests;
- JaCoCo coverage checks;
- PMD checks;
- packaging.

---

## Main Endpoint

### Process an order
http POST /api/orders/{orderId}/processOrder

Example response:
json { "id": 1 }


---

## Notes

The following constraints from the exercise were respected:

- database migrations were not modified;
- classes marked with `// WARN: Should not be changed during the exercise` were not modified;
- the refactoring was covered by automated tests;
- the application still passes the Maven verification lifecycle.
