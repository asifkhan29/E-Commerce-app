# Ecommerce Management System

A Spring Boot application for managing products, users, and orders with dynamic discount computation based on user type and order amount.

## Features

### 1. Product Management
- CRUD operations with soft delete
- Search/filter by name, price range, and availability
- Pagination and sorting
- Redis caching for performance

### 2. User Management
- JWT-based authentication
- Role-based access control (USER, PREMIUM_USER, ADMIN)
- Spring Security integration
- Password encryption with BCrypt

### 3. Order Management
- Place orders for multiple products
- Stock validation and inventory management
- Dynamic discount calculation using Strategy Pattern
- Discount rules:
  - USER: No discount
  - PREMIUM_USER: 10% off total order
  - Orders > $500: Extra 5% discount for any user

### 4. Technical Stack
- Java 17, Spring Boot 3.1.5
- Spring Data JPA, Spring Security, Spring Validation
- H2 (development) / PostgreSQL (production)
- Liquibase for database migrations
- Redis for caching
- JUnit 5, Mockito for testing
- OpenAPI 3 documentation
- Docker & Docker Compose
- Spring Actuator for monitoring
- SLF4J logging

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose (optional)

### Running Locally

```bash
# Clone the repository
git clone <repository-url>
cd ecommerce

# Build the project
mvn clean package

# Run the application
java -jar target/ecommerce-service-1.0.0.jar

# Access the application
http://localhost:8080

# Swagger UI
http://localhost:8080/swagger-ui.html

# H2 Console (dev)
http://localhost:8080/h2-console
