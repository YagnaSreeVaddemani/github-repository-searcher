
# GitHub Repository Searcher

A Java Spring Boot REST API application that searches GitHub repositories using the GitHub REST API and stores repository information in a PostgreSQL database.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- REST API
- GitHub REST API
- JUnit 5
- Mockito
- MockMvc
- Postman

## Features

- Search GitHub repositories using keywords.
- Filter repositories by programming language.
- Retrieve stored repository information.
- Filter repositories by minimum stars.
- Sort repositories by stars, forks, or last updated date.
- Store repository information in PostgreSQL.
- Handle API errors and invalid requests.
- Automated service and controller tests.

## Project Architecture

- Controller: Handles REST API requests.
- Service: Contains business logic.
- Client: Communicates with GitHub REST API.
- Repository: Performs database operations.
- Entity: Represents repository data.
- DTO: Transfers request and response data.
- Exception: Handles application errors.

## API Endpoints

### 1. Search repositories

POST `/api/github/search`

Example request:

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

### 2. Get all stored repositories

GET `/api/github/repositories`

### 3. Filter by language

GET `/api/github/repositories?language=Java`

### 4. Filter by minimum stars

GET `/api/github/repositories?minStars=100`

### 5. Sort repositories

GET `/api/github/repositories?sort=stars`

Supported sorting fields:

- stars
- forks
- updated

## Database

PostgreSQL is used to store GitHub repository information.

Configure your database connection in `application.properties` before running the application.

Do not commit database passwords or private credentials.

## Run the Application

1. Clone the repository.
2. Configure PostgreSQL.
3. Update database settings in `application.properties`.
4. Run the application using:

```bash
mvn spring-boot:run
```

## Run Tests

Execute:

```bash
mvn test
```

## Test Results

- Total tests: 11
- Failures: 0
- Errors: 0

## Author

Yagna Sree Vaddemani

- LinkedIn: https://www.linkedin.com/in/yagna-sree-vaddemani-284077361/
- GitHub: https://github.com/YagnaSreeVaddemani
