package com.ssafy.mugit.global.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.MessageDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class ApiMessageBus implements MessageBus {

    private final ObjectMapper objectMapper;
    private final String MESSAGE_SERVER_BASE_URI;
    private final String MESSAGE_SEND_URI;

    public ApiMessageBus(
            @Value("${message.api.base-uri}") String messageApiBaseUri,
            @Value("${message.api.send-uri}") String messageApiSendUri,
            @Autowired ObjectMapper objectMapper) {
        this.MESSAGE_SERVER_BASE_URI = messageApiBaseUri;
        this.MESSAGE_SEND_URI = messageApiSendUri;
        this.objectMapper = objectMapper;
    }

    @Async
    @SneakyThrows
    @Override
    public void send(Object message) {
        RestTemplate restTemplate = new RestTemplate();
        String body = objectMapper.writeValueAsString(message);
        log.info("알림 전송 : {}", body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        restTemplate.exchange("http://" + MESSAGE_SERVER_BASE_URI + MESSAGE_SEND_URI, HttpMethod.POST, entity, MessageDto.class, body);
    }
}
