package com.ssafy.mugit.musitory.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.musitory.entity.MugitoryRecord;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class MugitoryCustomRepositoryImpl implements MugitoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MugitoryCustomRepositoryImpl(@Autowired EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MugitoryRecord findByIdWithMugitoryRecord(Long id) {
        return null;
    }
}
