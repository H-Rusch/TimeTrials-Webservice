package com.hrusch.timetrials.webservice.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestConfiguration
public class KafkaTestcontainersConfig {

  @Container
  private static final KafkaContainer kafkaContainer = new KafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

  static {
    kafkaContainer.start();
    setKafkaProperties();
  }

  static void setKafkaProperties() {
    System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
  }
}
