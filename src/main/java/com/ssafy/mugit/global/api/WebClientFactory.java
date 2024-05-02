package com.ssafy.mugit.global.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactory {

    public WebClient getGoogleTokenWebClient() {
        return WebClient.builder()
                .build();
    }
}
