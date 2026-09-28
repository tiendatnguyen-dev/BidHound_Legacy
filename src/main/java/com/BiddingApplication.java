package com;

import com.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BiddingApplication {
  public static void main(String []args) {
    ApplicationContext applicationContext = SpringApplication.run(BiddingApplication.class, args);
  }
}
