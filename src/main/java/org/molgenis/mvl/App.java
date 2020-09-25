package org.molgenis.mvl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public RestTemplateBuilder restTemplateBuilder() {
    return new RestTemplateBuilder();
  }
}
