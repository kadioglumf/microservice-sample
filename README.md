# Microservice Sample

A sample microservices architecture using Spring Boot, Kafka, OAuth2, and WebSocket.

---

## Table of Contents

1. [About](#about)
2. [Architecture & Components](#architecture--components)
3. [Features](#features)
4. [Prerequisites](#prerequisites)
5. [Setup & Running](#setup--running)
6. [Services](#services)
7. [Inter-Service Communication](#inter-service-communication)
8. [Authentication & Security](#authentication--security)
9. [Usage / Examples](#usage--examples)

---

## About

This repository contains a sample microservices-based project demonstration.  
It implements multiple services communicating through Kafka, authentication via OAuth2, a gateway component, WebSocket-based real-time features, and a user interface for OAuth2 interactions.

---

## Architecture & Components

The solution includes:

| Component | Purpose |
|---|---|
| Gateway Service | Entry point to route requests to internal services, handles API aggregation and routing. |
| Auth Service | Manages authentication and authorization (OAuth2). |
| Data Service | Provides backend business logic / data storage / CRUD operations. |
| WebSocket Service | Provides real-time communication via WebSocket. |
| Kafka Event Router | Bridges inter-service events using Kafka topics. |
| OAuth2 UI | A user interface for login / authorization flows. |

---

## Features

- OAuth2-based authentication and authorization
- Microservices architecture with separated concerns
- Event-driven communication via Kafka
- Real-time communication using WebSocket
- API gateway / routing
- Inter-service event handling
- Dockerized services for easy deployment

---

## Prerequisites

Before you begin, ensure you have:

- Docker installed
- Docker Compose (often comes with Docker)
- Java 17+ (if you plan to run services locally without Docker)
- Maven (for building Java-based services)

---

## Setup & Running

### Using Docker Compose

From the `.dev` directory:

```bash
cd .dev
docker compose build
docker compose up -d
```

### Verifying

- Check running containers:
  ```bash
  docker ps
  ```
- View logs of a specific container:
  ```bash
  docker logs <container_name>
  ```
- To bring services down:
  ```bash
  docker compose down
  ```

---

## Services

Here are the main services and their roles:

- **Auth Service**: Handles user authentication, JWT / OAuth2 token issuance.
- **Gateway Service**: Routes incoming API calls to appropriate microservice, may handle cross-cutting concerns (logging, rate limit, etc.).
- **Data Service**: Core business logic, data persistence.
- **WebSocket Service**: Provides endpoints for real-time features (for example, push notifications or live updates).
- **Kafka Event Router**: Manages event publishing / subscribing among services using Kafka topics.
- **OAuth2 UI**: Front-end interface for login, consent screens, etc.

---

## Inter-Service Communication

- Kafka is used for decoupled communication between microservices.
- Messages / events flow through defined topics.
- Services listen or publish events as needed (e.g. Data Service publishes events, others consume).

---

## Authentication & Security

- OAuth2-based authentication managed via Auth Service.
- Gateway enforces token verification.
- Services expect valid JWT or OAuth2 access tokens in API requests.
- Role-based access control can be enforced in individual services.

---

## Usage / Examples

Here are example flows:

1. **User Login**  
   Use the OAuth2 UI to login. After successful login, receive access token.

2. **Calling a Service via Gateway**  
   Send authorized request to Gateway; Gateway forwards to Data Service or another downstream service.

3. **Real-time Message**  
   Connect WebSocket Service with valid token; receive push updates or live events published by other services via Kafka.

4. **Event-driven Reaction**  
   For example, Data Service publishes an event upon data change; Kafka Event Router forwards event; WebSocket Service pushes to connected clients.


