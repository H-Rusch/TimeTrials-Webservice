package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.model.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublishNewRecordKafkaProducerService {

  @Value("${spring.kafka.publish-new-record-topic}")
  private String topicName;

  private final KafkaTemplate<String, Time> kafkaTemplate;

  public void publish(Time time) {
    log.info("Publishing new record: {}", time);
    kafkaTemplate.send(topicName, time);
  }
}
