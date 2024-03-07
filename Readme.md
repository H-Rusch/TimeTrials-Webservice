# Time Trials Webservice

This application is a Spring Boot based webservice that provides a REST API to save and query times
driven in Mario Kart 8 Deluxe Time Trials.

This service uses Kafka to consume new times and to publish new records when the record for a track is broken.

A MongoDB database is being used to store the data.

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


