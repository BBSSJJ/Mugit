package com.ssafy.mugit.auth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("[테스트] 세션에 키만 설정하면 jsessionid가 설정되는지?")
    void testSessionMakeJSESSIONID() throws Exception {
        // given
        String uri = "/auth/test/jsessionid";

        // when
        ResultActions perform = mvc.perform(get(uri));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }
    
    @Test
    @DisplayName("[테스트] 같은 쿠키를 가진 session은 같은 key를 가지고 있는지?")
    void testGetSessionByJSESSIONID() throws Exception {
        // given
        String uri = "/auth/test/jsessionid";
        MvcResult mvcResult = mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        Cookie[] cookies = mvcResult.getResponse().getCookies();
        String sessionId = mvcResult.getResponse().getContentAsString();

        // when
        MvcResult result = mvc.perform(get(uri).cookie(cookies))
                .andExpect(status().isOk())
                .andReturn();
        String newSessionId = result.getResponse().getContentAsString();

        // then
        assertThat(sessionId).isEqualTo(newSessionId);
    }

    @Test
    @DisplayName("[테스트] 다른 세션은 ID가 다른지")
    void testAnotherSession() throws Exception {
        // given
        String uri = "/auth/test/jsessionid";
        MvcResult mvcResult = mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();
        String sessionId = mvcResult.getResponse().getContentAsString();

        // when
        MvcResult result = mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();
        String newSessionId = result.getResponse().getContentAsString();

        // then
        assertThat(sessionId).isNotEqualTo(newSessionId);
    }
}
