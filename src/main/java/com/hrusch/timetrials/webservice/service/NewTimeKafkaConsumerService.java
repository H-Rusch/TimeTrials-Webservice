package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.exception.TimeDtoValidationException;
import com.hrusch.timetrials.webservice.model.TimeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "service.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class NewTimeKafkaConsumerService {

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  private final TimeService timeService;
  private final Validator validator;

  @Autowired
  public NewTimeKafkaConsumerService(TimeService timeService, Validator validator) {
    this.timeService = timeService;
    this.validator = validator;
  }

  @KafkaListener(
      topics = "${spring.kafka.new-time-topic}",
      containerFactory = "kafkaListenerJsonFactory")
  public void receive(TimeDto timeDto) {
    log.info("Received new timeDto via Kafka: {}", timeDto);

    Set<ConstraintViolation<TimeDto>> validationErrors = validator.validate(timeDto);
    if (!validationErrors.isEmpty()) {
      log.error("Validation failed for incoming timeDto: {}", timeDto);
      throw new TimeDtoValidationException(validationErrors);
    }

    timeService.saveNewTime(timeDto);
  }
}
