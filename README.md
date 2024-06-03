# Exchange Rate Portal

## Description

This web application provides the following functionalities:

1. **Central Bank Exchange Rates Page**:
    - Exchange rates are pulled from the [Bank of Lithuania](https://www.lb.lt/webservices/FxRates/en/) using a Quartz cron job scheduled to run every afternoon.
      https://www.lb.lt/webservices/FxRates/FxRates.asmx?op=getCurrentFxRates
2. **Exchange Rate History**:
    - Displays the exchange rate history for a selected currency, with pagination support.

3. **Currency Converter**:
    - Converts entered amounts from one currency to another using the latest exchange rates retrieved from local database.

## Technologies Used

- **Java 17**
- **Maven**
- **Spring Boot 3.3.0**
- **Lombok**
- **H2 File Database**
- **IntelliJ IDEA**

## Logic

- Hibernate is used to generate tables if they do not already exist.
- ISO-4217 standard currency codes are downloaded from the International Organization for Standardization (ISO) webpage in XML format and parsed during the initial run if the table is not available in the database.
- Daily rate updates are conducted via a scheduled cron job using Quartz. Schedule can be modified from quartz.properties file.
- The currency converter uses the latest rates from the H2 file database for conversions.
- DTOs (Data Transfer Objects) are used to aggregate rates and currency name data.

## Backend Development

- The backend service handles fetching and storing exchange rates, converting currencies, and managing historical rate data.
- Exchange rates are fetched from the external service and stored in the H2 file database.
- Historical rates are paginated and can be queried by currency.
- The currency conversion logic handles conversions involving EUR and other currencies.

## Frontend

- A tailored UI built with Vue.js tools is available in a separate repository: [exchange-rate-portal-frontend](https://github.com/5OO/exchange-rate-portal-frontend).

## Getting Started

### Prerequisites

- Java 17
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/5OO/exchange-rate-portal
   cd exchange-rate-portal
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Configuration

- The application properties can be configured in `src/main/resources/application.properties`. Make sure that appropriate location for H2 file DB is specified in `spring.datasource.url` . Apache Tomcat WebServer is initialized to run on port 8080 (http)
- The Quartz job for fetching exchange rates is configured from `src/main/resources/quartz.properties` to run daily at a specified time.
- H2 default DB credentials are Username: sa (default), Password: (empty by default)
- H2 Console  is available for database management and query execution during development:

    URL: http://localhost:8080/h2 Ensure the JDBC URL matches the path configured in application.properties.



## Running Tests

- Unit tests are located in the `src/test/java` directory.
- To run tests, use:
  ```bash
  mvn test
  ```
 
## Acknowledgments

- The Bank of Lithuania for providing exchange rate data.
- The International Organization for Standardization for the ISO-4217 standard currency codes.
 