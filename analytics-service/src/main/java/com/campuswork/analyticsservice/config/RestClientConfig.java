package com.campuswork.analyticsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${job-service.url}")
    private String jobServiceUrl;

    @Value("${application-service.url}")
    private String applicationServiceUrl;

    @Value("${attendance-service.url}")
    private String attendanceServiceUrl;

    @Value("${payroll-service.url}")
    private String payrollServiceUrl;

    @Bean
    public RestClient jobServiceRestClient() {
        return RestClient.builder().baseUrl(jobServiceUrl).build();
    }

    @Bean
    public RestClient applicationServiceRestClient() {
        return RestClient.builder().baseUrl(applicationServiceUrl).build();
    }

    @Bean
    public RestClient attendanceServiceRestClient() {
        return RestClient.builder().baseUrl(attendanceServiceUrl).build();
    }

    @Bean
    public RestClient payrollServiceRestClient() {
        return RestClient.builder().baseUrl(payrollServiceUrl).build();
    }
}