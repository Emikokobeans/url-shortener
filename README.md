# URL Shortener

## Project Overview

This project is a minimal URL shortener built with Spring Boot for the backend and Vue.js for the frontend. The application accepts a full URL, generates a shortened link with a random alias or accepts a custom alias, and returns the result through a simple REST API and web form.

A minimal URL shortener built with:

- **Backend:** Java, Spring Boot
- **Frontend:** Vue 3, Vite, Vitest
- **Containerisation:** Docker, Docker Compose

## Project layout

```text
backend: Spring Boot API
frontend: Vue app with Vitest
```

## Run with Docker

```bash
docker compose up --build
```

Then open:

- Frontend UI: http://localhost:8082
- Backend API: http://localhost:8080
- Backend API health check: http://localhost:8080/

## Backend

### Installation

```bash
cd backend
mvn clean install
```
### Run the backend

```bash
cd backend
mvn test
mvn spring-boot:run
```

### Test the API

```bash
curl -X POST http://localhost:8080/shorten \
  -H 'Content-Type: application/json' \
  -d '{"fullUrl":"https://example.com/some/long/path"}'
```

```bash
curl -X POST http://localhost:8080/shorten \
  -H 'Content-Type: application/json' \
  -d '{"fullUrl":"https://example.com/some/long/path", "customAlias":"short"}'
```

```bash
curl -X POST http://localhost:8080/shorten \
  -H 'Content-Type: application/json' \
  -d '{"fullUrl":"bad-url"}'
```

## Frontend

### Installation

```bash
cd frontend
npm install
```
### Run the frontend

```bash
npm run test
npm run dev
```

## Design and implementation decisions

### Iterative Design

The first version only included the core shortening flow:

- accept a full URL
- generate a random alias
- return a shortened URL

Keeping the initial scope small makes the project easier to test and iterate on, while leaving room for later additions such as persistence, custom aliases, redirect endpoints, and delete/list operations.

### Docker-based local environment

The project includes Docker and Docker Compose so the full stack can be run with a single command. This makes local setup more repeatable and gives a deployment-friendly structure.

## Future improvements

### Robust error handling

Further enhance validation of inputs and handle errors gracefully. Improvve the Frontend so it shows errors from the API appropriately.

### General UI/UX improvements

Improve the appearance and usability of the frontend application. Enable the user to copy the shortened link to their clipboard in a single click.

### Generated API models

Rather than hand-writing DTOs, the backend generates request and response models from the OpenAPI specification during the build. This can reduce boilerplate and ensures the Java models stay consistent with the API contract.

### CORS configuration

Instead of hard-coding `@CrossOrigin`, use a global CORS configuration and make it configurable so it doesn't require further code changes in the future.

### Data persistance

The current MVP stores shortened URLs in memory, which means the data is lost when the application restarts. A next step would be to persist URL mappings in a database such as PostgreSQL or MySQL.


## Notes

- Github Copilot: Used for unit test assistance/setup and general error diagnosis.
- Vue.js: Setup scaffolding added using `npm create vue@latest`
- Time on task: ~8 hours