package com.ssafy.mugit.record.service;

import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SourceCronService {

    private final SourceRepository sourceRepository;

    public void cleanSource() {
        // 1. 미사용 소스 id 리스트 추출
        List<Source> notUsedList = sourceRepository.findNotUsed();

        // 2. 소스 테이블 정리
        sourceRepository.deleteAll(notUsedList);
    }

}
