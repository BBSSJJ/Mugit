package com.ssafy.mugit.record.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.ssafy.mugit.record.entity.QRecord.record;
import static com.ssafy.mugit.record.entity.QRecordSource.recordSource;
import static com.ssafy.mugit.record.entity.QSource.source;

@Repository
public class CustomRecordRepositoryImpl implements CustomRecordRepository {
    private final JPAQueryFactory queryFactory;

    public CustomRecordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Record findByIdWithSources(Long recordId) {
        return queryFactory.select(record)
                .from(record)
                .leftJoin(record.recordSources, recordSource).fetchJoin()
                .leftJoin(recordSource.source, source).fetchJoin()
                .where(record.id.eq(recordId))
                .fetchOne();
    }
}
