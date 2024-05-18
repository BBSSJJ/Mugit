package com.ssafy.mugit.global.message;

import com.ssafy.mugit.global.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component("ApiMessageBus")
@Slf4j
public class ApiMessageBus implements MessageBus {

    private final String MESSAGE_SERVER_BASE_URI;
    private final String MESSAGE_SEND_URI;
    private final WebClient.Builder webClientBuilder;

    public ApiMessageBus(
            @Value("${message.api.base-uri}") String messageApiBaseUri,
            @Value("${message.api.send-uri}") String messageApiSendUri,
            @Autowired WebClient.Builder webClientBuilder) {
        this.MESSAGE_SERVER_BASE_URI = messageApiBaseUri;
        this.MESSAGE_SEND_URI = messageApiSendUri;
        this.webClientBuilder = webClientBuilder;
    }

    @Async
    @Override
    public void send(Object message) {
        webClientBuilder
                .baseUrl("http://" + MESSAGE_SERVER_BASE_URI).build()
                .post()
                .uri(MESSAGE_SEND_URI)
                .contentType(APPLICATION_JSON)
                .bodyValue(message)
                .retrieve().bodyToMono(MessageDto.class)
                .doOnError((error) -> log.info("알림전송 실패 : {}", error.getMessage()))
                .subscribe((response) -> log.info("SSE 서버 응답 : {}", response.getMessage()));
    }
}
