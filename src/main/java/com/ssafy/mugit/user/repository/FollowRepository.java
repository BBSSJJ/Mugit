package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.entity.Follow;
import com.ssafy.mugit.user.repository.querydsl.FollowCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {
}
