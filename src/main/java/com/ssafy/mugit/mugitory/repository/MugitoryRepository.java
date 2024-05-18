package com.ssafy.mugit.mugitory.repository;

import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.mugitory.repository.querydsl.MugitoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MugitoryRepository extends JpaRepository<Mugitory, UserDate>, MugitoryCustomRepository {
}
