# Time Trials API

This application is a web-service that provides a REST API and uses JPA to connect to a postgres database.

The service's purpose is to save times driven in Mario Kart 8 Deluxe Time Trials.

## Swagger UI
This project features a Swagger/ OpenAPI documentation.

It is available through the Swagger-UI at `/swagger-ui/index.html`.

## Database configurations
To connect to a postgres database, the following environment variables have to be set:

- `POSTGRES_URL` jdbc url for the database
- `POSTGRES_USER` postgres user to authenticate with the database
- `POSTGRES_PASSWORD` password of postgres user
