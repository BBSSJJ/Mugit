package com.ssafy.mugit.global.message;

import com.ssafy.mugit.global.config.Publisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("RedisMessageBus")
@Slf4j
@RequiredArgsConstructor
public class RedisMessageBus implements MessageBus {

    private final Publisher publisher;

    @SneakyThrows
    @Async
    @Override
    public void send(Object message) {
        publisher.publish(message);
    }
}
