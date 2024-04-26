package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.repository.querydsl.ProfileCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileCustomRepository {
    Boolean existsByNickName(String nickName);
}
