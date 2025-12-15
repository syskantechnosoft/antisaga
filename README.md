# Online Shopping Application (Microservices + SAGA)

This is a **Microservices-based Online Shopping Application** built with **Spring Boot 3.3.0**, **JDK 21**, and **Angular**. It implements the **SAGA Design Pattern (Choreography)** for distributed transactions.

## Services Architecture

| Service | Port | Description |
| :--- | :--- | :--- |
| **Discovery Service** | 8761 | Eureka Server for Service Discovery |
| **Config Server** | 8888 | Centralized Configuration |
| **API Gateway** | 8080 | Entry point, Routing, CORS |
| **Order Service** | 8083 | Order Management (SAGA Initiator) |
| **Payment Service** | 8084 | Payment Processing (SAGA Participant) |
| **Inventory Service** | 8085 | Inventory Management (SAGA Participant) |
| **User Service** | 8081 | User Authentication & Management |
| **Product Service** | 8082 | Product Catalog (Search & Filter) |
| **Notification Service** | 8086 | Email Notification System |
| **Shipping Service** | 8087 | Shipping Management |
| **Frontend** | 4200 | Angular Application (Cart, Auth, Product Cards) |

## Technology Stack

- **Backend:** Spring Boot, Spring Cloud (Eureka, Gateway, Config), Spring Data JPA.
- **Messaging:** RabbitMQ (Event-driven SAGA).
- **Database:** MySQL (Production/Docker).
- **Frontend:** Angular (Latest) with standalone components.
- **Containerization:** Docker & Docker Compose.

## SAGA Pattern Implementation (Choreography)

The application uses an event-driven approach:

1.  **Order Creation:**
    *   User places an order (via Cart).
    *   Order saved as `ORDER_CREATED`.
    *   Event published to `order-exchange`.
2.  **Payment Processing:**
    *   `Payment Service` listens to `order-exchange`.
    *   Deducts balance from User.
    *   Publishes `PAYMENT_COMPLETED` or `PAYMENT_FAILED`.
3.  **Inventory Reservation:**
    *   `Inventory Service` listens to `payment-exchange` (PAYMENT_COMPLETED).
    *   Iterates through items and reserves stock.
    *   Publishes `INVENTORY_RESERVED` or `INVENTORY_REJECTED`.
4.  **Shipping & Completion:**
    *   `Shipping Service` listens to `INVENTORY_RESERVED`.
    *   Creates Shipment and publishes `SHIPPING_STARTED` event.
    *   `Order Service` listens to events updates status to `ORDER_COMPLETED`.
    *   `Notification Service` listens to `SHIPPING_STARTED` and sends email.

## Execution Plan & Setup

### Prerequisites
- JDK 21
- Docker Desktop
- Maven
- Node.js & NPM (for Frontend)

### Step 1: Build Backend
Run the following command in the root directory to build all microservices:
```bash
mvn clean install -DskipTests
```

### Step 2: Start Infrastructure & Services
Start the entire system using Docker Compose:
```bash
docker-compose up --build
```
*Note: Wait for all services to register with Eureka (http://localhost:8761).*

### Step 3: Frontend Setup
1. Initialize Frontend:
   ```bash
   cd frontend
   npm install
   ng serve
   ```
2. Navigate to `http://localhost:4200`.

### Step 4: Application Flow
1.  **Register:** Go to `/register` and create a user (e.g., testuser/password).
2.  **Login:** Go to `/login`.
3.  **Browse:** View Products in `/products`. Use search/filter.
4.  **Cart:** Add items to cart. Go to `/cart`.
5.  **Checkout:** Click Checkout.
6.  **Verify:** Check Console for "SAGA Initiated" and Backend logs for "Email Sent".

## Data Initialization
*   **Users:** ID 1 has detailed balance. (Register new user -> ID auto-gen).
*   **Products:** Dummy data inserted on startup (Laptop, Smartphone, etc.).

## Troubleshooting
- **Service Not Found:** Check Eureka.
- **RabbitMQ:** Check `http://localhost:15672` (guest/guest).
