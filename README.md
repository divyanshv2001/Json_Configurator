JSON Dataset API (Spring Boot + H2)

This is a Spring Boot application that manages JSON dataset records.
It exposes two REST APIs:

Insert Record API → insert a JSON record into a dataset

Query API → query records with groupBy or sortBy parameters

The project uses:

Java 17+

Spring Boot 3

Spring Data JPA

H2 in-memory database

🚀 Getting Started
Prerequisites

Java 17 or higher

Maven 3.9+

Clone the repository
git clone https://github.com/your-username/json-dataset-api.git
cd json-dataset-api

Run the application
mvn spring-boot:run


The app will start at:

http://localhost:8080


H2 Console available at:

http://localhost:8080/h2


JDBC URL: jdbc:h2:mem:datasets

User: sa

Password: password

📌 API Endpoints
1. Insert Record API

Method: POST
URL: /api/dataset/{datasetName}/record
Body: raw JSON object

Example
POST http://localhost:8080/api/dataset/employee_dataset/record
Content-Type: application/json

{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}

Response
{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}

2. Query API — Group By

Method: GET
URL: /api/dataset/{datasetName}/query?groupBy={field}

Example
GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department

Response
{
  "groupedRecords": {
    "Engineering": [
      { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" },
      { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" }
    ],
    "Marketing": [
      { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" }
    ]
  }
}

3. Query API — Sort By

Method: GET
URL: /api/dataset/{datasetName}/query?sortBy={field}&order=asc|desc

Example
GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc

Response
{
  "sortedRecords": [
    { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" },
    { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" },
    { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
  ]
}

🧪 Running Tests

Unit and integration tests are included.

Run all tests:

mvn test


Run specific test:

mvn -Dtest=DatasetApiSmokeTest test

📂 Project Structure
src/main/java/com/example/demo/
 ├─ JsonDatasetApiApplication.java        # Main Spring Boot app
 ├─ controller/
 │   └─ DatasetController.java            # REST endpoints
 ├─ domain/
 │   ├─ model/
 │   │   └─ DatasetRecord.java            # JPA entity
 │   └─ service/
 │       ├─ DatasetService.java           # Service interface
 │       └─ DatasetServiceImpl.java       # Service implementation
 ├─ repository/
 │   └─ DatasetRecordRepository.java      # JPA repository
 └─ dto/
     ├─ InsertRecordResponse.java
     ├─ QueryResponseGrouped.java
     └─ QueryResponseSorted.java

src/test/java/com/example/demo/
 ├─ JsonDatasetApiApplicationTests.java   # Context load test
 └─ DatasetApiSmokeTest.java              # API smoke test

📝 Notes

Database is in-memory (H2). Data resets when the app stops.

Unique constraint: (datasetName, recordId) ensures no duplicate IDs per dataset.

APIs are REST-compliant and testable in Postman.
