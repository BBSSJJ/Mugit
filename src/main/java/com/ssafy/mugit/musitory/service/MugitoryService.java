package com.ssafy.mugit.musitory.service;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.musitory.entity.Mugitory;
import com.ssafy.mugit.musitory.repository.MugitoryRepository;
import com.ssafy.mugit.record.entity.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.global.exception.error.MugitoryError.ALREADY_RECORDED_TO_MUGITORY;

@Service
@RequiredArgsConstructor
public class MugitoryService {
    private final MugitoryRepository mugitoryRepository;

    @Transactional
    public void deleteRecord(Mugitory mugitory, Record record) {
        try {
            mugitory.deleteRecord(record);
        } catch (MugitoryException e) {
            switch (e.getMugitoryError()) {
                case DELETE_ALL_RECORD_IN_MUGITORY -> {
                    mugitoryRepository.delete(mugitory);
                }
                case DELETE_RECORD_NOT_IN_MUGITORY -> throw new UserApiException(UserApiError.DELETE_RECORD_NOT_IN_MUGITORY);
            }
        }
    }

    @Transactional
    public void recordMugitory(Record record, Mugitory mugitory) {
        try {
            mugitory.addRecord(record);
        } catch (MugitoryException e) {
            if (e.getMugitoryError().equals(ALREADY_RECORDED_TO_MUGITORY))
                throw new UserApiException(UserApiError.ALREADY_RECORDED_TO_MUGITORY);
        }
    }
}
