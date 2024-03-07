package com.hrusch.timetrials.webservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.hrusch.timetrials.webservice.model.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class PublishNewRecordKafkaProducerServiceTest {

  @Mock
  private KafkaTemplate<String, Time> kafkaTemplate;

  @InjectMocks
  private PublishNewRecordKafkaProducerService subject;

  @Test
  void givenTime_whenPublish_thenVerifyKafkaPublish() {
    // given
    Time time = Time.builder().build();

    // when
    subject.publish(time);

    // then
    verify(kafkaTemplate).send(any(), eq(time));
  }
}