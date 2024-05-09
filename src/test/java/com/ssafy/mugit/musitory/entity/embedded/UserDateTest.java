package com.ssafy.mugit.musitory.entity.embedded;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserDateTest {

    @Test
    @DisplayName("사용자와 날짜로 생성")
    void createUserDate() {
        // given
        long userId = 1;
        LocalDate now = LocalDate.now();

        // when
        UserDate userDate = new UserDate(userId);

        // then
        assertThat(userDate.getUserId()).isEqualTo(userId);
        assertThat(userDate.getDate()).isEqualTo(now);
    }
}