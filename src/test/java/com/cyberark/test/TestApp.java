package com.cyberark.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;

@ComponentScan(basePackages = "com.cyberark")
public class TestApp {

    @Bean
    public WebTestClient webTestClient(@Value("${local.server.port}") int port) {
        return WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/api/v1").build();
    }

}
