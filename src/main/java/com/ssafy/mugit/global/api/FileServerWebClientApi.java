package com.ssafy.mugit.global.api;

import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.PathsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
@Slf4j
public class FileServerWebClientApi {
    private final WebClient.Builder webClientBuilder;
    private final String fileServerUrl;

    public FileServerWebClientApi(@Autowired WebClient.Builder webClientBuilder,
                                  @Value("${file-server}") String fileServerUrl) {
        this.webClientBuilder = webClientBuilder;
        this.fileServerUrl = fileServerUrl;
    }

    public Mono<MessageDto> sendFilePaths(List<String> filePaths) {
        URI uri = UriComponentsBuilder
                .fromUriString("http://" + fileServerUrl + "/files/delete")
                .build()
                .toUri();

        PathsDto pathsDto = new PathsDto(filePaths);

        return webClientBuilder.build()
                .method(HttpMethod.DELETE)
                .uri(uri)
                .body(Mono.just(pathsDto), PathsDto.class)
                .retrieve()
                .bodyToMono(MessageDto.class)
                .doOnSuccess(response -> {
                    log.info(response.getMessage());
                })
                .doOnError(error -> {
                    log.error("파일 정리 실패, {}", error.getMessage());
                });
    }
}
