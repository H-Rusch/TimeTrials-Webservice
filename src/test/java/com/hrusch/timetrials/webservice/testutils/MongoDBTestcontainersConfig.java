package com.hrusch.timetrials.webservice.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestConfiguration
public class MongoDBTestcontainersConfig {

  static final String TAG = "6.0.19";
  static final String MONGODB_IMAGE = "mongo:%s".formatted(TAG);

  @Container
  private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGODB_IMAGE);

  static {
    mongoDBContainer.start();
    setMongoProperties();
  }

  static void setMongoProperties() {
    System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getConnectionString());
  }
}
