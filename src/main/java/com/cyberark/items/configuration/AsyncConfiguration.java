package com.cyberark.items.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Profile("async")
@EnableAsync
@Configuration
public class AsyncConfiguration {
}
