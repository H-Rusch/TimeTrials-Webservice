Feature: Add Time via REST
  New times submitted through a REST-request should be able to be inserted into the database

  Background:
    Given configured path to be cit/add-time

  Scenario: Database contains time after its submitted through POST request
    Given database does not contain time name_BabyPark_1-20-123.json
    When posting new time TimeDto_name_BabyPark_1-20-123.json
    Then response code is 201
    And database contains time name_BabyPark_1-20-123.json
