# Store Management API

A Spring Boot REST API for managing store products with authentication and role-based access control.

## 🚀 Quick Start

```bash
# Clone and run
git clone https://github.com/KeeBy023/Store-Management-API.git
cd store-management-api
mvn spring-boot:run
```

Access the API at `http://localhost:8080`

## 📋 Overview

This API provides a complete solution for store management with:

- **Product Catalog Management** - Full CRUD operations with search and pagination
- **Inventory Control** - Real-time stock tracking, adjustments, and alerts
- **Price History Auditing** - Automatic tracking of all price changes
- **Role-Based Security** - Admin and user access levels
- **Business Logic Validation** - Prevents invalid operations like negative inventory

## 🔐 Authentication

HTTP Basic Authentication with predefined users:

| Username | Password | Role | Permissions |
|----------|----------|------|-------------|
| `admin` | `admin` | ADMIN | Full access to all operations |
| `user` | `password` | USER | Read-only access to products |

## 📚 API Reference

### Product Management

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/products` | USER/ADMIN | List products with pagination and search |
| `GET` | `/api/products/{id}` | USER/ADMIN | Get specific product details |
| `POST` | `/api/products` | ADMIN | Create new product |
| `PATCH` | `/api/products/{id}` | ADMIN | Update product fields |
| `DELETE` | `/api/products/{id}` | ADMIN | Remove product |
| `GET` | `/api/products/{id}/price-history` | USER/ADMIN | View price change history |

### Inventory Operations

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/inventory/low-stock` | ADMIN | Find products below threshold |
| `GET` | `/api/inventory/out-of-stock` | ADMIN | Find products with zero stock |
| `POST` | `/api/inventory/{id}/adjust` | ADMIN | Adjust stock with reason |
| `POST` | `/api/inventory/{id}/restock` | ADMIN | Add inventory |
| `POST` | `/api/inventory/{id}/reserve` | ADMIN | Reserve stock for orders |

## 🏗️ Architecture

### Technology Stack
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with HTTP Basic Auth
- **Database**: H2 (in-memory)
- **Documentation**: OpenAPI 3 / Swagger
- **Testing**: JUnit 5, Mockito
- **Build**: Maven

### Key Features
- **Event-Driven Architecture**: Uses Spring events for loose coupling
- **Transactional Safety**: Price changes and inventory updates are atomic
- **Audit Trail**: Complete history of price changes with user tracking
- **Input Validation**: Comprehensive validation with meaningful error messages
- **Exception Handling**: Global error handling with proper HTTP status codes
- **Pagination**: All list endpoints support pagination and sorting

## 📖 API Documentation

Interactive API documentation available at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## 🗄️ Database

**H2 In-Memory Database**
- **Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:storedb`
- **Username**: `sa`
- **Password**: (empty)

### Schema
- `products` - Product catalog with pricing and inventory
- `price_history` - Audit trail of all price changes

## 🚦 Business Rules

- **Inventory**: Cannot go below zero
- **Prices**: Must be positive values
- **Price History**: Automatically recorded on product creation and any price change with user tracking
- **Validation**: Comprehensive input validation on all endpoints

## 📈 Future Enhancements

- [ ] Product categories and tags
- [ ] Supplier management
- [ ] Order processing integration
- [ ] Advanced reporting and analytics
- [ ] JWT Authentication and OAuth