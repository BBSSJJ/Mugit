package com.ssafy.mugit.global.web.api;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@Slf4j
public class OAuthRestTemplateApi implements OAuthApi {
    private final String userInfoUrl;

    public OAuthRestTemplateApi(@Value("${oauth.google.userinfo.url}") String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    @Override
    public UserInfoDto getUserInfo(String token, SnsType snsType) {
        switch (snsType) {
            case GOOGLE:
                return getGoogleUserInfo(token);
            default:
                throw new UserApiException(UserApiError.ILLEGAL_SNS_TYPE);
        }
    }

    private UserInfoDto getGoogleUserInfo(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(headers);
        URI uri = UriComponentsBuilder
                .fromUriString(userInfoUrl)
                .queryParam("access_token", token)
                .build()
                .toUri();
        registErrorHandler(restTemplate);
        return restTemplate.exchange(uri, HttpMethod.GET, request, UserInfoDto.class).getBody();
    }

    private void registErrorHandler(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(401)))
                    throw new UserApiException(UserApiError.UNAUTHORIZED_BY_OAUTH);
            }
        });
    }
}
