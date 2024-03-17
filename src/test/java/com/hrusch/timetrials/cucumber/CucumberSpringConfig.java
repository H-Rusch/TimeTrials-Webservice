package com.hrusch.timetrials.cucumber;

import com.hrusch.timetrials.webservice.SpringApplication;
import com.hrusch.timetrials.webservice.testutils.KafkaTestcontainersConfig;
import com.hrusch.timetrials.webservice.testutils.MongoDBTestcontainersConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@SpringBootTest(
    classes = SpringApplication.class,
    webEnvironment = WebEnvironment.RANDOM_PORT,
    useMainMethod = UseMainMethod.ALWAYS
)
@Import({
    KafkaTestcontainersConfig.class,
    MongoDBTestcontainersConfig.class
})
public class CucumberSpringConfig {

}
