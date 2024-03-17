package com.hrusch.timetrials.webservice.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestConfiguration
public class MongoDBTestcontainersConfig {

  @Container
  private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

  static {
    mongoDBContainer.start();
    setMongoProperties();
  }

  static void setMongoProperties() {
    System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getConnectionString());
  }
}
