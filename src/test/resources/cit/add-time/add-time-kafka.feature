Feature: Add Time via Kafka
  New times submitted through Kafka should be able to be inserted into the database

  Background:
    Given configured filepath to be cit/add-time

  Scenario: Database contains time after its submitted through Kafka
    Given database does not contain time name_BabyPark_1-20-123.json
    When writing Kafka message TimeRequest_name_BabyPark_1-20-123.json
    Then database contains time name_BabyPark_1-20-123.json
