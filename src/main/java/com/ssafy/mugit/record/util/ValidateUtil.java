package com.ssafy.mugit.record.util;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateUtil {

    private final FlowRepository flowRepository;
    private final RecordRepository recordRepository;

    public void validateFlow(Long userId, Long flowId) {
        Flow flow = flowRepository.findByIdWithUser(flowId);
        if (flow == null || !userId.equals(flow.getUser().getId())) {
            throw new UserApiException(UserApiError.NOT_ALLOWED_ACCESS);
        }
    }

    public Record validateRecord(Long userId, Long recordId) {
        Record record = recordRepository.findByIdWithUser(recordId);
        if(record == null || !userId.equals(record.getFlow().getUser().getId())) {
            throw new UserApiException(UserApiError.NOT_ALLOWED_ACCESS);
        }
        return record;
    }

}
