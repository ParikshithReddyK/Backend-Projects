package com.campuswork.applicationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${job-service.url}")
    private String jobServiceUrl;

    @Bean
    public RestClient jobServiceRestClient() {
        return RestClient.builder()
                .baseUrl(jobServiceUrl)
                .build();
    }
}