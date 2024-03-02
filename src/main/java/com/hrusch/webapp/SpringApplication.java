package com.hrusch.webapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringApplication {

  public static void main(String[] args) {
    org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
  }
}
