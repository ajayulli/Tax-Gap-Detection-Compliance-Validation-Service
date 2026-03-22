# Tax Gap Detection & Compliance Validation Service

## Overview
A backend service built with Spring Boot 3 and Java 17 that audits financial transactions, calculates tax gaps, and runs dynamic, database-driven compliance rules.

## Architecture
- **Layered Design**: Controller -> Service -> Repository.
- **Rule Engine**: Evaluates JSON-configured rules stored in the database dynamically.
- **Audit Logging**: Asynchronous logging of Ingestion, Tax Computation, and Rule Execution.

## Tech Stack
- Java 17, Spring Boot 3, Spring Security
- Spring Data JPA, H2 In-Memory DB (for dev)
- JUnit 5 & Mockito (Coverage: >50%)

## How to Run
1. Clone the repository: `git clone [URL]`
2. Ensure Maven and Java 17 are installed.
3. Run: `mvn spring-boot:run`

## Database Setup
The application uses Hibernate to automatically generate the schema (`spring.jpa.hibernate.ddl-auto=update`). 
Pre-filled User data and Rule configurations are loaded via `src/main/resources/data.sql`.

## Sample Postman Call (Upload Transactions)
**POST** `http://localhost:8080/api/v1/transactions/upload`
**Auth**: Basic Auth (user: admin, pass: password)
**Body**:
```json
[
  {
    "transactionId": "TXN-1001",
    "date": "2023-10-01",
    "customerId": "CUST-55",
    "amount": 15000.00,
    "taxRate": 0.10,
    "reportedTax": 1400.00,
    "transactionType": "SALE"
  }
]
