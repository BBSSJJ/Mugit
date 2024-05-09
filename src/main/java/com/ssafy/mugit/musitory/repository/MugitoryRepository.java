package com.ssafy.mugit.musitory.repository;

import com.ssafy.mugit.musitory.entity.Mugitory;
import com.ssafy.mugit.musitory.repository.querydsl.MugitoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MugitoryRepository extends JpaRepository<Mugitory, Long>, MugitoryCustomRepository {
}
