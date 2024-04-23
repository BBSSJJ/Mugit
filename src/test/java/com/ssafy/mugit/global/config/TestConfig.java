package com.ssafy.mugit.global.config;

import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public AcceptanceTestExecutionListener dataCleaner() {
        return new AcceptanceTestExecutionListener();
    }
}
