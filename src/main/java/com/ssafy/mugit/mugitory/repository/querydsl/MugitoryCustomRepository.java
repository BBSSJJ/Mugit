package com.ssafy.mugit.mugitory.repository.querydsl;

import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.record.entity.Record;

import java.util.List;
import java.util.Optional;

public interface MugitoryCustomRepository {
    List<ResponseMugitoryDto> findOneYearMugiotory(Long userId);

    List<ResponseMugitoryRecordDto> findRecordDtosById(UserDate userDateId);

    Optional<Mugitory> findByRecord(Record record);
}
