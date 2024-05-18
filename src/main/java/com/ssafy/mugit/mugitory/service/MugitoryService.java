package com.ssafy.mugit.mugitory.service;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.MugitoryError;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.mugitory.repository.MugitoryRepository;
import com.ssafy.mugit.record.entity.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MugitoryService {
    private final MugitoryRepository mugitoryRepository;

    @Transactional
    public void recordMugitory(Record record) {
        try {
            Mugitory mugitory = mugitoryRepository.findById(new UserDate(record.getFlow().getUser().getId()))
                    .orElseThrow(() -> new MugitoryException(MugitoryError.FIRST_RECORD_TODAY));
            // 오늘의 뮤지토리에 레코드 추가
            mugitory.addRecord(record);
        } catch (MugitoryException e) {
            log.info(e.getMessage());
            // 첫 레코드 새로 생성
            if (e.getMugitoryError() == MugitoryError.FIRST_RECORD_TODAY) mugitoryRepository.save(new Mugitory(record));
        }
    }

    @Transactional
    public void deleteRecordMugitory(Record record) {
        try {
            record.getMugitory().deleteRecord(record);
        } catch (MugitoryException e) {
            mugitoryRepository.delete(record.getMugitory());
        }
    }

    public List<ResponseMugitoryDto> getOneYearMugitoryByUserId(Long userId) {
        return mugitoryRepository.findOneYearMugiotory(userId);
    }

    public List<ResponseMugitoryRecordDto> getMugitoryRecordByDate(Long userId, String date) {
        List<ResponseMugitoryRecordDto> recordDtos = mugitoryRepository.findRecordDtosById(new UserDate(userId, LocalDate.parse(date)));
        if (recordDtos.isEmpty()) throw new UserApiException(UserApiError.NOT_EXIST_MUGITORY);
        return recordDtos;
    }
}
