package com.hrusch.timetrials.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.testutils.TestDataReader;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;


public class StepDefinitions {

  private static final String COLLECTION = "times";
  private static final String BASE_PATH = "/times";

  @LocalServerPort
  private int port;
  @Value("${spring.kafka.new-time-topic}")
  private String newTimeTopic;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  private TestDataReader testDataReader;
  private RequestSpecification request;
  private Response response;

  @Before
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
            .jackson2ObjectMapperFactory((cls, charset) -> objectMapper));

    request = RestAssured.given()
        .port(port)
        .basePath(BASE_PATH);
  }

  // Given
  @Given("configured path to be {word}")
  public void configurePath(String path) {
    testDataReader = new TestDataReader(path.split("/"));
  }

  @Given("database does not contain time {word}")
  public void databaseDoesNotContainTime(String filename) {
    Time time = convertToTime(testDataReader.readFileToString(filename));

    Query query = new Query(
        new Criteria().andOperator(
            Criteria.where("username").is(time.getUsername()),
            Criteria.where("track").is(time.getTrack().name()),
            Criteria.where("duration").is(time.getDuration())));

    mongoTemplate.remove(query, COLLECTION);
  }

  @Given("times are stored in the database")
  public void timesAreStoredInTheDatabase(List<String> filenames) {
    filenames.forEach(this::timeIsStoredInTheDatabase);
  }

  @Given("time {word} is stored in the database")
  public void timeIsStoredInTheDatabase(String filename) {
    String json = testDataReader.readFileToString(filename);

    mongoTemplate.save(convertToTime(json), COLLECTION);
  }

  // When
  @When("request is made to {word} without giving a username")
  public void requestIsMadeToEndpointWithoutUsername(String endpoint) {
    response = request.get(endpoint);
  }

  @When("request is made to {word} with the username {word}")
  public void requestIsMadeToEndpointWithUsername(String endpoint, String username) {
    response = request.param("username", username).get(endpoint);
  }

  @When("posting new time {word}")
  public void postingNewTime(String filename) {
    String timeDtoJson = testDataReader.readFileToString(filename);

    response = request.contentType(String.valueOf(ContentType.APPLICATION_JSON))
        .body(timeDtoJson)
        .post();
  }

  @When("writing Kafka message {word}")
  public void writingKafkaMessage(String filename) {
    TimeDto timeDto = convertToTimeDto(testDataReader.readFileToString(filename));

    kafkaTemplate.send(newTimeTopic, timeDto);

    await()
        .pollDelay(Duration.ofSeconds(3))
        .until(() -> true);
  }

  // Then
  @Then("response code is {int}")
  public void responseCodeIs(int code) {
    assertThat(response.statusCode())
        .isEqualTo(code);
  }

  @Then("response contains the following times")
  public void responseContainsTimes(List<String> timeFiles) {
    List<Time> expectedTimes = timeFiles.stream()
        .map(testDataReader::readFileToString)
        .map(this::convertToTime)
        .toList();

    assertThat(response.body().jsonPath().getList("", Time.class))
        .containsExactlyInAnyOrderElementsOf(expectedTimes);
  }

  @Then("response is the time {word}")
  public void responseContainsTimes(String timeFile) {
    Time expectedTime = convertToTime(testDataReader.readFileToString(timeFile));

    assertThat(response.body().jsonPath().getObject("", Time.class))
        .isEqualTo(expectedTime);
  }

  @Then("database contains time {word}")
  public void databaseContainsTime(String filename) {
    Time expectedTime = convertToTime(testDataReader.readFileToString(filename));

    Query query = new Query(
        new Criteria().andOperator(
            Criteria.where("username").is(expectedTime.getUsername()),
            Criteria.where("track").is(expectedTime.getTrack().name()),
            Criteria.where("duration").is(expectedTime.getDuration())));

    List<Time> result = mongoTemplate.find(query, Time.class, COLLECTION);

    assertThat(result).hasSize(1);
    assertThat(result.get(0))
        .extracting(
            Time::getUsername,
            Time::getTrack,
            Time::getCombination,
            Time::getDuration)
        .containsExactly(
            expectedTime.getUsername(),
            expectedTime.getTrack(),
            expectedTime.getCombination(),
            expectedTime.getDuration());
  }

  @SneakyThrows
  private Time convertToTime(String jsonString) {
    return objectMapper.readValue(jsonString, Time.class);
  }

  @SneakyThrows
  private TimeDto convertToTimeDto(String jsonString) {
    return objectMapper.readValue(jsonString, TimeDto.class);
  }
}
