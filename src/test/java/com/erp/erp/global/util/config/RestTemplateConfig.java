package com.erp.erp.global.util.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    HttpClient httpClient = HttpClients.custom().build();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
      public boolean hasError(ClientHttpResponse response) {
        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
      }
    });
    return restTemplate;
  }
}
