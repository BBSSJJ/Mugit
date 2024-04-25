package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.querydsl.UserDtoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserDtoRepository {
    User findBySnsIdAndSnsType(String snsId, SnsType snsType);
}
