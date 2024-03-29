package com.hrusch.timetrials.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.timetrials.webservice.model.Time;
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
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;


public class StepDefinitions {

  private static final String COLLECTION = "times";
  private static final String BASE_PATH = "/times";

  @LocalServerPort
  private int port;

  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ObjectMapper objectMapper;

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

  @Given("configured path to be {word}")
  public void configurePath(String path) {
    testDataReader = new TestDataReader(path.split("/"));
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

  @When("request is made to {word} without giving a username")
  public void requestIsMadeToEndpointWithoutUsername(String endpoint) {
    response = request.get(endpoint);
  }

  @When("request is made to {word} with the username {word}")
  public void requestIsMadeToEndpointWithUsername(String endpoint, String username) {
    response = request.param("username", username).get(endpoint);
  }

  @Then("response contains the following times")
  public void responseContainsTimes(List<String> timeFiles) {
    List<Time> expectedTimes = timeFiles.stream()
        .map(testDataReader::readFileToString)
        .map(this::convertToTime)
        .toList();

    assertThat(response.statusCode())
        .isEqualTo(HttpStatus.OK.value());
    assertThat(response.body().jsonPath().getList("", Time.class))
        .containsExactlyInAnyOrderElementsOf(expectedTimes);
  }

  @Then("response is the time {word}")
  public void responseContainsTimes(String timeFile) {
    Time expectedTime = convertToTime(testDataReader.readFileToString(timeFile));

    assertThat(response.statusCode())
        .isEqualTo(HttpStatus.OK.value());
    assertThat(response.body().jsonPath().getObject("", Time.class))
        .isEqualTo(expectedTime);
  }

  @SneakyThrows
  private Time convertToTime(String jsonString) {
    return objectMapper.readValue(jsonString, Time.class);
  }
}
