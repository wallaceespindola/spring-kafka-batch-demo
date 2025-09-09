# Spring Kafka Batch Demo

A Spring Boot 3 application (Java 21) demonstrating:

- Kafka message production and consumption
- Persisting messages with Spring Data JPA (H2 in-memory DB)
- Joining pairs of messages with Spring Batch on a scheduled job
- REST endpoints to produce and query data

## Tech Stack

- Java 21, Spring Boot 3.3
- Spring Web, Spring Kafka, Spring Data JPA, Spring Batch
- H2 in-memory database
- Lombok for boilerplate reduction
- JUnit 5 + Mockito + AssertJ for testing
- Docker multi-stage image

## Requirements

- JDK 21
- Maven 3.9+
- Docker (optional) and Docker Compose (optional)

## Configuration

Default configuration is in `src/main/resources/application.properties`.
Important properties:

- `server.port=8080`
- `spring.kafka.bootstrap-servers=localhost:9092`
- `app.kafka.topic=demo.messages`
- H2 console at `/h2`

You can override any property via environment variables, e.g.:

```sh
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

## Build and Run (Local JVM)

```sh
# Run tests
mvn test

# Package (fat jar under target/)
mvn -DskipTests package

# Run
java -jar target/spring-kafka-batch-demo-0.0.1-SNAPSHOT.jar
```

Ensure Kafka is available at `spring.kafka.bootstrap-servers` (defaults to `localhost:9092`).
You can start a local Kafka and Zookeeper with the provided `docker-compose.yml`:

```sh
docker compose up -d zookeeper kafka
```

## Docker

A multi-stage Dockerfile is provided at `Dockerfile`.

```sh
# Build image
docker build -t spring-kafka-batch-demo:latest .

# Run (Linux host example, reaching Kafka on the host)
docker run --rm -p 8080:8080 \
  --add-host=host.docker.internal:host-gateway \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  spring-kafka-batch-demo:latest
```

### Docker Compose (app + Kafka)

`docker-compose.yml` includes Zookeeper and Kafka. To add the app service, extend it like below:

```yaml
services:
  app:
    build: .
    image: spring-kafka-batch-demo:latest
    depends_on:
      - kafka
    environment:
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    ports:
      - "8080:8080"
```

Then run:

```sh
docker compose up --build
```

## Endpoints

Base path: `http://localhost:8080`

- Produce messages
  - `GET /api/produce` — produce a pair of messages (2 parts) with the same correlationId
  - `GET /api/produce/{n}` — produce `n` pairs

- Query data
  - `GET /api/messages` — list all messages
  - `GET /api/joined` — list all joined records

- Actuator
  - `GET /actuator/health`

## Batch Job

- A scheduled job runs every 30s (`@Scheduled(fixedRate = 30_000)`) and joins pairs of messages
- The job writes a `JoinedRecordEntity` and marks the two `MessageEntity` records as processed

## Postman Collection

A collection is available under `postman/spring-kafka-batch-demo.postman_collection.json`.
It uses a `baseUrl` variable (defaults to `http://localhost:8080`).
Import it in Postman, or set `baseUrl` to match your environment.

## Tests

Run all tests:

```sh
mvn test
```

Included tests:

- `JoinServiceTest` — verifies join logic and persistence actions
- `MessageConsumerTest` — verifies consumed payload is persisted
- `QueryControllerTest` — verifies listing endpoints
- `ProducerControllerTest` — verifies two messages are sent and response payload

## Lombok

Lombok is configured in `pom.xml` with annotation processing. If your IDE flags errors, enable annotation processing in your IDE settings.

## H2 Console

- Enabled at `/h2`, default JDBC URL: `jdbc:h2:mem:demo` (see `application.properties`)

## Notes

- When running inside Docker, adjust `SPRING_KAFKA_BOOTSTRAP_SERVERS` accordingly.
- For Linux Docker hosts, `--add-host=host.docker.internal:host-gateway` is handy to reach host services.
