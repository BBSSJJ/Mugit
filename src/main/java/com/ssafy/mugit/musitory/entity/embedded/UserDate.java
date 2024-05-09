package com.ssafy.mugit.musitory.entity.embedded;


import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDate {

    private Long userId;
    private LocalDate date;

    public UserDate(long userId) {
        this.userId = userId;
        this.date = LocalDate.now();
    }

    public UserDate(long userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }
}
