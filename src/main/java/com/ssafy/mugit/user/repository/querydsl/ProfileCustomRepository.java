package com.ssafy.mugit.user.repository.querydsl;

import com.ssafy.mugit.user.entity.Profile;

public interface ProfileCustomRepository {
    Profile findByUserId(Long userId);
}
