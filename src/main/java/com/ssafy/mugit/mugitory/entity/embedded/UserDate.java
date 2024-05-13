package com.ssafy.mugit.mugitory.entity.embedded;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDate {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "date", nullable = false)
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
