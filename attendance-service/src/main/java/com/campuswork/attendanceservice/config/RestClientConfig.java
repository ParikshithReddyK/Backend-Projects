package com.campuswork.attendanceservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${shift-service.url}")
    private String shiftServiceUrl;

    @Bean
    public RestClient shiftServiceRestClient() {
        return RestClient.builder().baseUrl(shiftServiceUrl).build();
    }
}