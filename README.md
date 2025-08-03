# Time Trials Webservice

Spring Boot based webservice to store and query times driven in Mario Kart 8 Deluxe Time Trials.

This service provides a REST API to store or query times. Additionally, it uses Kafka to consume
new times and to publish new records when the record for a track is broken. MongoDB is used to store
the data.

## Build

Running the tests requires a running Podman
instance,since [Testcontiners](https://testcontainers.com/) are used for Kafka and the MongoDB.
The tests can be started using

```sh
mvn verify
```

This application is set up to be run with Podman. The `Dockerfile` will start the service by running
the services jar. Therefore, the jar has to be created first by running

```sh
mvn package
```

### Run locally

Docker Compose can be used in order to test the application and all dependencies locally. The full
environment is defined in `compose.yaml` and can be started by running

```sh
podman-compose up
```

## Swagger UI

This project features a Swagger/ OpenAPI documentation.

It is available through the Swagger-UI at `/swagger-ui/index.html`.

## Configuration

The webservice can be configured through environment variables. The following table shows the
different variables that can be set.

| Environment variable name  | Description                                                                                     | 
|----------------------------|-------------------------------------------------------------------------------------------------|
| `MONGODB_URI`              | fully specified URI of the database                                                             | 
| `CONSUMER_GROUP`           | group id for the Kafka consumer                                                                 |
| `BOOTSTRAP_SERVERS`        | list of Kafka servers to connect to                                                             |
| `NEW_TIME_TOPIC`           | name of the Kafka topic which is used for incoming new times                                    |
| `PUBLISH_NEW_RECORD_TOPIC` | name of the Kafka topic which is used for outgoing times which break the record for their track |
