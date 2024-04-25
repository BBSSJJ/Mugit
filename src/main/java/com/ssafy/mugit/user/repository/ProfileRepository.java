package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Boolean existsByNickName(String nickName);
}
