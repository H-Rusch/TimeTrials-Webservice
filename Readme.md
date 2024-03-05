# Time Trials Webservice

This application is a Spring Boot based webservice that provides a REST API to save and query times driven in Mario Kart 8 Deluxe Time Trials. 

A MongoDB database is being used to hold the data.

## Swagger UI
This project features a Swagger/ OpenAPI documentation.

It is available through the Swagger-UI at `/swagger-ui/index.html`.

## Database configuration
To connect to a postgres database, the following environment variables have to be set:

- `MONGODB_URL` fully specified URL of the database
