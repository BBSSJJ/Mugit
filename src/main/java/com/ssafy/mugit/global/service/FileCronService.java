package com.ssafy.mugit.global.service;

import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.api.FileServerWebClientApi;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileCronService {
    private final ProfileRepository profileRepository;
    private final FlowRepository flowRepository;
    private final SourceRepository sourceRepository;
    private final FileServerWebClientApi fileServerWebClientApi;

    @Value("${upload-path}")
    private String UPLOAD_PATH;

    public void sendDeleteRequest() {
        log.info("[[[ 파일 정리 요청 전송 ]]]");
        fileServerWebClientApi.sendFilePaths(searchAllFileName()).subscribe(response -> {
            log.info("[[ 파일 정리 요청 응답 ]] : " + response.getMessage());
        });
    }

    private List<String> searchAllFileName() {
        List<String> filePaths = new ArrayList<>();
        profileRepository.findAll().forEach(profile -> {
            String filePath = profile.getProfileImagePath();
            if (filePath.length() > 25) {
                String fileName = filePath.substring(25);
                if (!fileName.contains("default")) {
                    filePaths.add(UPLOAD_PATH + fileName);
                }
            }
        });

        flowRepository.findAll().forEach(flow -> {
            String coverPath = flow.getCoverPath();
            String musicPath = flow.getMusicPath();
            if (coverPath.length() > 25) {
                String coverName = coverPath.substring(25);
                if (!coverName.contains("default")) {
                    filePaths.add(UPLOAD_PATH + coverName);
                }
            }
            if (musicPath.length() > 25) {
                String musicName = musicPath.substring(25);
                filePaths.add(UPLOAD_PATH + musicName);
            }
        });

        sourceRepository.findAll().forEach(source -> {
            String filePath = source.getPath();
            if (filePath.length() > 25) {
                String fileName = filePath.substring(25);
                if (!fileName.contains("default")) {
                    filePaths.add(UPLOAD_PATH + fileName);
                }
            }
        });

        System.out.println(filePaths);

        return filePaths;
    }
}
