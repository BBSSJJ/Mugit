package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.querydsl.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    User findBySnsIdAndSnsType(String snsId, SnsType snsType);

    @Query("SELECT f.followee FROM com.ssafy.mugit.user.entity.Follow f " +
            "LEFT JOIN users u ON f.follower.id = u.id " +
            "WHERE u = :user")
    List<User> findFolloweesByUser(User user);
}
