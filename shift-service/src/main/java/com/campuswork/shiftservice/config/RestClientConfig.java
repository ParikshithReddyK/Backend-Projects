package com.campuswork.shiftservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${application-service.url}")
    private String applicationServiceUrl;

    @Bean
    public RestClient applicationServiceRestClient() {
        return RestClient.builder().baseUrl(applicationServiceUrl).build();
    }
}