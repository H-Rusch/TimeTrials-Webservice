package com.hrusch.timetrials.cucumber;

import com.hrusch.timetrials.webservice.testutils.TestDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


public class StepDefinitions {

  private static final String COLLECTION = "times";

  private TestDataReader testDataReader;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Given("time {word} is stored in the database")
  public void timeIsStoredInTheDatabase(String filename) {
    //String json = testDataReader.
  }

  @When("testing")
  public void testing() {
    System.out.println("testing");
    System.out.println(mongoTemplate);
  }

  @Then("assert something")
  public void assertSomething() {
    System.out.println("as");
  }
}
