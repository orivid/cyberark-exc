package com.cyberark.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = "com.cyberark")
public class TestApp {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
