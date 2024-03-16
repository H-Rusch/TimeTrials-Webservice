package com.hrusch.timetrials.cucumber;

import com.hrusch.timetrials.webservice.testutils.TestDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefinitions {

  @Autowired
  private TestDataReader testDataReader;

  @Given("time {word} is stored in the database")
  public void timeIsStoredInTheDatabase(String filename) {
    //String json = testDataReader.
  }

  @When("testing")
  public void testing() {
    System.out.println("testing");
  }

  @Then("assert something")
  public void assertSomething() {
    System.out.println("as");
  }

}
