package com.hrusch.webservice.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration
public class MongoDBTestContainerConfig {

  @Container
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
      .withExposedPorts(27017);

  @DynamicPropertySource
  static void mongoProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString);
  }

  @BeforeAll
  static void setUp() {
    mongoDBContainer.start();
  }

  @AfterAll
  static void tearDown() {
    mongoDBContainer.stop();
  }
}
