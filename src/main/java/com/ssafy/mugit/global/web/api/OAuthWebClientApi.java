package com.ssafy.mugit.global.web.api;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.web.WebClientFactory;
import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class OAuthWebClientApi implements OAuthApi {
    private final WebClientFactory webClientFactory;
    private final String userInfoUrl;

    public OAuthWebClientApi(@Autowired WebClientFactory webClientFactory,
                             @Value("${oauth.google.userinfo.url}") String userInfoUrl) {
        this.webClientFactory = webClientFactory;
        this.userInfoUrl = userInfoUrl;
    }

    @Override
    public UserInfoDto getUserInfo(String token, SnsType snsType) {
        switch (snsType) {
            case GOOGLE:
                return getGoogleUserInfo(token).block();
            default:
                throw new UserApiException(UserApiError.ILLEGAL_SNS_TYPE);
        }
    }

    private Mono<UserInfoDto> getGoogleUserInfo(String token) {
        URI uri = UriComponentsBuilder
                .fromUriString(userInfoUrl)
                .queryParam("access_token", token)
                .build()
                .toUri();

        return webClientFactory.getGoogleTokenWebClient()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserInfoDto.class)
                .doOnError(error -> {
                    log.error("Oauth 인증 실패, {}", error.getMessage());
                    throw new UserApiException(UserApiError.UNAUTHORIZED_BY_OAUTH);
                });
    }
}
