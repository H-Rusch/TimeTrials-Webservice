package com.hrusch.timetrials.webservice.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestConfiguration
public class KafkaTestcontainersConfig {

  private static final String TAG = "7.8.0";
  private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:%s".formatted(TAG);

  @Container
  private static final KafkaContainer kafkaContainer = new KafkaContainer(
      DockerImageName.parse(KAFKA_IMAGE));

  static {
    kafkaContainer.start();
    setKafkaProperties();
  }

  static void setKafkaProperties() {
    System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
  }
}
