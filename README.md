# URL Shortener API

A **production-ready** URL shortener service built with **Java**, **Spring Boot**, and **Docker**.

- **RESTful** endpoints for URL shortening and redirection.
- **Redis** caching for fast lookups.
- **Rate limiting** (Bucket4j) to prevent abuse.
- **Environment-specific** profiles (dev, prod) for easy configuration.
- **Testing** (unit + integration).
- **CI/CD** with GitHub Actions.

---

## Overview

This application accepts a long URL, generates a short code, and then redirects any requests for that short code back to the original URL.

---

## Features

1. **RESTful API**:
    - `POST /api/shortener`: Shorten a URL.
    - `GET /{shortCode}`: Redirect to the original URL.
2. **Data Persistence**:
    - Uses **Spring Data JPA** with an H2 in-memory database for development.
3. **Caching**:
    - **Redis** integration for caching short code lookups, reducing DB load.
4. **Rate Limiting**:
    - **Bucket4j** filter to throttle requests (per IP).
5. **Validation & Exception Handling**:
    - `@Valid` annotations to validate input.
    - Global exception handler for consistent error responses.
6. **Testing**:
    - Unit tests.
    - Integration tests (MockMvc + H2).
7. **Docker**:
    - Dockerfile for containerizing the application.
    - `docker-compose.yml` to run with Redis.
8. **CI/CD**:
    - GitHub Actions workflow (`.github/workflows/maven.yml`) to build, test, and build a Docker image on every push/PR.

---
## API Design

### Endpoints

1. **Shorten a URL**
    - **Endpoint:** `POST /api/shortener`
    - **Request Body (JSON):**
      ```json
      {
        "url": "https://www.example.com"
      }
      ```
    - **Response (JSON):**
      ```json
      {
        "shortCode": "abc123"
      }
      ```
    - **Description:**  
      The server stores the mapping (`originalUrl` → `shortCode`) in the database (and caches it in Redis).  
      If the same URL has already been shortened, the existing `shortCode` will be returned.

2. **Redirect to the Original URL**
    - **Endpoint:** `GET /{shortCode}`
    - **Description:**  
      Looks up the original URL associated with `shortCode`. If found, responds with an HTTP **302** (Found) redirect to the original URL. Otherwise, returns **404 Not Found**.

### Data Model

- **UrlMapping**
    - **id** (Long): Auto-generated primary key.
    - **originalUrl** (String): The long URL provided by the client.
    - **shortCode** (String): A Base62-encoded.

### Sequence Diagram

Below is **sequence diagram** demonstrating how the process works from end to end:

```mermaid
sequenceDiagram
    participant C as Client
    participant API as URL Shortener API
    participant DB as Database / Redis Cache

    C->>API: POST /api/shortener<br/>{ "url": "https://www.example.com" }
    API->>DB: Save / retrieve mapping
    DB-->>API: shortCode (e.g., "abc123")
    API-->>C: { "shortCode": "abc123" }

    C->>API: GET /abc123
    API->>DB: Lookup original URL
    DB-->>API: "https://www.example.com"
    API-->>C: 302 Redirect to original
