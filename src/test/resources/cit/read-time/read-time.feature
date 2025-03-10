Feature: Read times
  Times stored in the database should be accessible via HTTP request

  Background:
    Given configured filepath to be cit/read-time
    And times are stored in the database
      | name_BabyPark_1-08-480.json          |
      | name_BabyPark_1-09-480.json          |
      | name_MarioCircuit_1-46-434.json      |
      | otherName_MarioCircuit_1-46-434.json |
      | otherName_BabyPark_1-07-480.json     |

  Scenario: Getting times for a user should return only times for that user
    When request is made to /best with the username name
    Then response code is 200
    And response contains the following times for tracks
      | Track         | File                            |
      | BABY_PARK     | name_BabyPark_1-08-480.json     |
      | MARIO_CIRCUIT | name_MarioCircuit_1-46-434.json |

  Scenario: Getting times without giving a user should return the best times on the tracks
    When request is made to /best without giving a username
    Then response code is 200
    And response contains the following times for tracks
      | Track         | File                                 |
      | BABY_PARK     | otherName_BabyPark_1-07-480.json     |
      | MARIO_CIRCUIT | name_MarioCircuit_1-46-434.json      |
      | MARIO_CIRCUIT | otherName_MarioCircuit_1-46-434.json |

  Scenario: Getting best time for track for specific user should return the best the user has driven on that track
    When request is made to /best/BABY_PARK_GCN with the username name
    Then response code is 200
    And response contains the following times
      | name_BabyPark_1-08-480.json |

  Scenario: Getting best time for track should return the best time for track
    When request is made to /best/BABY_PARK_GCN without giving a username
    Then response code is 200
    And response contains the following times
      | otherName_BabyPark_1-07-480.json |
