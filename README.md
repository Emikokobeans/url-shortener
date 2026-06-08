# URL Shortener

A minimal URL shortener built with:

- **Backend:** Java, Spring Boot
- **Frontend:** Vue 3, Vite, Vitest

## Project layout

```text
backend: Spring Boot API
frontend: Vue app with Vitest
```

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
