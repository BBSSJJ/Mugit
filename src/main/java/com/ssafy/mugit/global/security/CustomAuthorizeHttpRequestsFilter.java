package com.ssafy.mugit.global.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import static com.ssafy.mugit.user.entity.type.RoleAuthority.DEFAULT_ADMIN_AUTHORITY;

@Component
public class CustomAuthorizeHttpRequestsFilter {
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> regist() {
        return (authorize) -> authorize

                // 관리자 전계정 조회
                .requestMatchers("/api/admin/sessions").hasAuthority(DEFAULT_ADMIN_AUTHORITY.getAuthority())

                // 사용자 로그인 및 회원가입
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/regist").permitAll()

                // 다른 사용자 조회
                .requestMatchers("/api/users/*/profiles/detail").permitAll()
                .requestMatchers("/api/users/nick/**").permitAll()
                .requestMatchers("/api/mugitories/**").permitAll()

                // 가짜 로그인 및 회원가입
                .requestMatchers("/api/users/mocks/**").permitAll()

                // 구글 Mock Controller
                .requestMatchers("/oauth2/v2/userinfo").permitAll()

                // flow 관련 API 인증 해제
                .requestMatchers("/api/flows/**").permitAll()

                // review Get 요청 API 인증 해제
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()

                // like 요청 API 인증 해제
                .requestMatchers("/api/likes/**").permitAll()

                // 이외 API 요청은 전부 인증 필요
                .requestMatchers("/api/**").authenticated()

                // 이외 요청은 전부 허용
                .anyRequest().permitAll();
    }
}
