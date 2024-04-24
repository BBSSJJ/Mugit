package com.ssafy.mugit.auth.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.test.web.reactive.server.WebTestClient;

@Tag("login")
@AcceptanceTest
public class AuthAcceptanceTest {

    WebTestClient webClient;

    @BeforeEach
    void setup() {
//        webClient = WebTestClient
//                .bindToController(new UserRegistController(userRegistService))
//                .controllerAdvice(new GlobalExceptionHandler())
//                .configureClient()
//                .baseUrl("/api/users/")
//                .build();

    }


}
