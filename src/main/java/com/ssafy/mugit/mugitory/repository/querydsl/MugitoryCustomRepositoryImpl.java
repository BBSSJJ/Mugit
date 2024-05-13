package com.ssafy.mugit.mugitory.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.mugitory.dto.response.QResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.QResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.record.entity.QRecord;
import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ssafy.mugit.mugitory.entity.QMugitory.mugitory;
import static com.ssafy.mugit.record.entity.QRecord.record;

public class MugitoryCustomRepositoryImpl implements MugitoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MugitoryCustomRepositoryImpl(@Autowired EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ResponseMugitoryDto> findOneYearMugiotory(Long userId) {
        return queryFactory.select(new QResponseMugitoryDto(mugitory))
                .from(mugitory)
                .where(mugitory.userDateId.userId.eq(userId)
                        .and(mugitory.userDateId.date.after(LocalDate.now().minusYears(1))))
                .stream().toList();
    }

    @Override
    public List<ResponseMugitoryRecordDto> findRecordDtosById(UserDate userDateId) {
        return queryFactory.select(new QResponseMugitoryRecordDto(record))
                .from(record)
                .where(record.mugitory.userDateId.eq(userDateId))
                .stream().toList();
    }

    @Override
    public Optional<Mugitory> findByRecord(Record record) {
        return queryFactory.selectFrom(mugitory)
                .where(mugitory.eq(record.getMugitory()))
                .stream().findFirst();
    }
}
