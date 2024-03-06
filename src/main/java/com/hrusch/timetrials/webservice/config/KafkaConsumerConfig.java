package com.hrusch.timetrials.webservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.timetrials.webservice.model.TimeDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@ConditionalOnProperty(name = "service.kafka.enabled", havingValue = "true")
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  private final ObjectMapper objectMapper;

  @Autowired
  public KafkaConsumerConfig(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> configMap = new HashMap<>();
    configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    return configMap;
  }

  @Bean
  public ConsumerFactory<String, TimeDto> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerConfigs(),
        new StringDeserializer(),
        new JsonDeserializer<>(TimeDto.class, objectMapper));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TimeDto> kafkaListenerJsonFactory() {
    ConcurrentKafkaListenerContainerFactory<String, TimeDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    return factory;
  }
}
