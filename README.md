# Stock Service

## Project Overview

Stock Service is a microservice responsible for managing stock inventory in an event-driven architecture. Developed using Java, Spring Boot, and PostgreSQL, this service communicates with other microservices via Apache Kafka and provides RESTful APIs for stock management.

## Features

- **Event-Driven Architecture**: This service leverages an event-driven architecture to enable asynchronous communication with other microservices. By using Apache Kafka, the Stock Service listens to events published by other services, such as Order Service. For instance, when an order is placed, an event indicating the new order is published to Kafka. The Stock Service consumes this event to update inventory levels accordingly. This architecture helps to decouple services, allowing them to operate independently and react to events in real time. It enhances scalability and resilience by distributing the workload and avoiding tight integrations.
- **RESTful APIs**: Provides endpoints for adding, retrieving, updating, and removing stock.
- **PostgreSQL Database**: Uses PostgreSQL for efficient data storage and retrieval.
- **Kafka Integration**: Produces and consumes Kafka events to reflect stock changes based on incoming messages from other services.

## Project Structure

The project is organized into the following main packages:

- **`config`**: Configures Kafka consumers and producers, and application settings.
- **`consumer`**: Contains Kafka listeners that process incoming messages and update stock.
- **`handler`**: Includes classes that handle messages from Kafka consumers and interact with service classes.
- **`controller`**: Manages REST API requests and responses for stock operations.
- **`event`**: Defines event classes used for Kafka messaging.
- **`exception`**: Custom exception classes for error handling.
- **`factory`**: Provides factory classes for creating event instances.
- **`model`**: Defines domain models representing stock items.
- **`repository`**: Contains JPA repositories for database interactions.
- **`service`**: Implements business logic related to stock management.

## API Endpoints

- **GET /stocks**: Retrieve all stocks.
- **GET /stocks/{id}**: Retrieve a stock by ID.
- **POST /stocks**: Create a new stock.
- **PUT /stocks/{id}**: Update an existing stock.
- **DELETE /stocks/{id}**: Remove a stock by ID.
