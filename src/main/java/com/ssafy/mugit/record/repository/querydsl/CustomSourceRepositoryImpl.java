package com.ssafy.mugit.record.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.record.entity.Source;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.mugit.record.entity.QRecordSource.recordSource;
import static com.ssafy.mugit.record.entity.QSource.source;

@Repository
public class CustomSourceRepositoryImpl implements CustomSourceRepository {

    private final JPAQueryFactory queryFactory;

    public CustomSourceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Source> findNotUsed() {
        return queryFactory.selectFrom(source)
                .leftJoin(recordSource).on(recordSource.source.id.eq(source.id))
                .where(recordSource.source.id.isNull())
                .fetch();
    }
}
