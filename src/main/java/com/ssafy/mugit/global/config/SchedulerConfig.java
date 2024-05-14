package com.ssafy.mugit.global.config;

import com.ssafy.mugit.global.service.FileCronService;
import com.ssafy.mugit.record.service.SourceCronService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final SourceCronService sourceCronService;
    private final FileCronService fileCronService;

    @Scheduled(cron = "0 0 * * * *")
    public void sourceJob() {
        log.info("[[[ 소스 테이블 작업 스케쥴링 시작 ]]]");
        sourceCronService.cleanSource();
        log.info("[[[ 소스 테이블 작업 스케쥴링 종료 ]]]");

        fileCronService.sendDeleteRequest();
    }

}
