package com.campuswork.payrollservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${job-service.url}")
    private String jobServiceUrl;

    @Value("${attendance-service.url}")
    private String attendanceServiceUrl;

    @Bean
    public RestClient jobServiceRestClient() {
        return RestClient.builder().baseUrl(jobServiceUrl).build();
    }

    @Bean
    public RestClient attendanceServiceRestClient() {
        return RestClient.builder().baseUrl(attendanceServiceUrl).build();
    }
}