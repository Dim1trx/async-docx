# Async DOCX Generation

Async DOCX Generation is a study project that validates the core backend flow for a future SaaS document-generation service.

The project demonstrates a Spring Boot API, Kafka-based asynchronous processing, a worker service, PostgreSQL persistence, Flyway migrations, and DOCX output generation.

## Architecture

```text
Client
  |
  | REST
  v
document-api
  | stores document data and generation status
  | publishes generation request
  v
Kafka
  |
  | consumes generation request
  v
document-worker
  | fills a service agreement template
  | writes generated DOCX to local storage
  | updates generation status
  v
PostgreSQL + filesystem
```

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Flyway
- PostgreSQL 18
- Apache Kafka 4.1.0
- Apache POI 5.5.1
- Testcontainers
- Maven Wrapper

## Services

- `document-api`: exposes REST endpoints and publishes Kafka messages.
- `document-worker`: consumes Kafka messages and generates `.docx` files.

Both services use the same PostgreSQL schema. The generated document file is stored on the local filesystem, while PostgreSQL stores metadata, status, timestamps, failure reason, and the generated file path.

## Status Flow

```text
QUEUED -> PROCESSING -> COMPLETED
                    \-> FAILED
```

The worker ignores duplicate events for generations that are already `COMPLETED` or `FAILED`.

## Running Locally

Start infrastructure:

```bash
docker compose up -d
```

Run the API:

```bash
cd document-api
./mvnw spring-boot:run
```

Run the worker in another terminal:

```bash
cd document-worker
./mvnw spring-boot:run
```

On Windows, use:

```bat
mvnw.cmd spring-boot:run
```

## API Examples

Create document data:

```bash
curl -X POST http://localhost:8080/api/v1/document-data \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Acme Corp",
    "providerName": "Async DOCX LLC",
    "serviceDescription": "Generate monthly service reports.",
    "effectiveDate": "2026-08-01",
    "feeAmount": 199.90
  }'
```

Request document generation:

```bash
curl -X POST http://localhost:8080/api/v1/document-generations \
  -H "Content-Type: application/json" \
  -d '{
    "documentDataId": "replace-with-document-data-id"
  }'
```

Check generation status:

```bash
curl http://localhost:8080/api/v1/document-generations/replace-with-generation-id
```

Download the completed DOCX:

```bash
curl -OJ http://localhost:8080/api/v1/document-generations/replace-with-generation-id/download
```

## Template

The worker uses a reviewable text template at:

```text
document-worker/src/main/resources/templates/service-agreement-template.txt
```

The template contains placeholders such as:

- `{{clientName}}`
- `{{providerName}}`
- `{{serviceDescription}}`
- `{{effectiveDate}}`
- `{{feeAmount}}`

The worker renders those placeholders and writes the final result as a DOCX file using Apache POI.

## Testing

Run API tests:

```bash
cd document-api
./mvnw test
```

Run worker tests:

```bash
cd document-worker
./mvnw test
```

The test suite includes unit tests for document data creation and DOCX generation. The Spring context tests use Testcontainers and run with Docker when available.

## Current Trade-offs

This project is intentionally simple. It is not production-ready SaaS infrastructure.

Known limitations:

- No authentication or authorization.
- No tenant isolation.
- No object storage; generated files are stored locally.
- No transactional outbox between PostgreSQL and Kafka.
- No retry policy or dead-letter topic.
- No uploadable DOCX templates.
- No observability beyond Spring Actuator health/info endpoints.

These omissions keep the project focused on validating the asynchronous document-generation workflow. In a production SaaS, the next steps would be object storage, tenant-aware data modeling, idempotent processing, outbox/event relay, retry and dead-letter handling, authentication, and richer document-template management.
