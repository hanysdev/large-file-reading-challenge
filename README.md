# Large File Reader

This project reads large CSV files containing yearly temperature data and processes them to provide the average temperature per city and year.

## Problem
We needed to efficiently read large files, process them, and provide a simple API for accessing the average temperature per city and year. The main challenge was ensuring high performance while maintaining scalability and ease of maintenance.

## Solution
- **CSV File Processing**: The data is read from a CSV file, and each city’s temperature data is grouped by year. The average temperature is calculated for each city-year combination.
- **Async File Watching**: An async service watches for changes in the file and processes new data whenever the file is updated.
- **API Endpoint**: The REST API provides an endpoint to fetch the average temperature for a specific city, with the city field hidden in the response.

## Steps to Run the Project
1. Navigate to the project directory:
   ```sh
   cd ./recruitment-challenges/large-file-reading-challenge/
   ```
2. Build the project using Gradle.
3. Configure the file path in the `application.yml` file to point to your CSV file.
4. Run the application using `./gradlew bootRun`.
5. Access the API at `http://localhost:8080/api/v1/temperature/average/{city}`.

## Test Coverage
- **Unit Tests**: We wrote unit tests for the core services using `JUnit` and `Mockito` to mock external dependencies.
- **Integration Tests**: We wrote integration tests to ensure the API controller works as expected.

## Issues and Resolutions
- **Large File Handling**: Initially, the file was read entirely into memory. We later moved to a streaming approach to handle larger files.
- **Hidden City Data**: The city field was required for filtering, but should not be exposed in the response. We used `@JsonIgnore` to ensure this data is excluded from the JSON response.

