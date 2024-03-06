package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.model.TimeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewTimeKafkaConsumerService {

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  private final TimeService timeRepository;
  private final Validator validator;

  @Autowired
  public NewTimeKafkaConsumerService(TimeService timeService, Validator validator) {
    this.timeRepository = timeService;
    this.validator = validator;
  }

  @KafkaListener(
      topics = "${spring.kafka.new-time-topic}",
      containerFactory = "kafkaListenerJsonFactory")
  public void receive(@Valid TimeDto timeDto) {
    log.info("Received new timeDto via Kafka: {}", timeDto);

    Set<ConstraintViolation<TimeDto>> validationErrors = validator.validate(timeDto);
    if (!validationErrors.isEmpty()) {
      log.error("Validation failed for incoming timeDto: {}", timeDto);
      throw new ValidationException(String.valueOf(validationErrors));
    }

    timeRepository.saveNewTime(timeDto);
  }
}
