# Order & Inventory Microservices

This project contains two Spring Boot microservices:

1. **Inventory Service (8081)** – manages products and inventory batches.
2. **Order Service (8082)** – places orders and calls the Inventory Service to deduct stock.

Both services use Spring Boot, JPA, H2, and communicate via RestTemplate.

---

## How to Run

### Inventory Service
cd inventory-service
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run

Runs at: http://localhost:8081

### Order Service
cd order-service
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run

Runs at: http://localhost:8082

---

## API Endpoints

### Inventory Service
GET  /inventory/{productId}
POST /inventory/update
Body example: {"productId":1, "quantity":5}

### Order Service
POST /order
Body example: {"productId":1, "quantity":5}

---

## H2 Consoles
Inventory: http://localhost:8081/h2-console  
Order:     http://localhost:8082/h2-console  

---

## Tests
Run tests:
.\mvnw.cmd test

Coverage report:
I have used IDE(IntelliJ) specific coverage tools, but we can use jacoco too, but for this its plugin needs to be added.
