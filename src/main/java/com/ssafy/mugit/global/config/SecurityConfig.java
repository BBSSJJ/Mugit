package com.ssafy.mugit.global.config;

import com.ssafy.mugit.global.security.CustomAccessDenialHandler;
import com.ssafy.mugit.global.security.CustomAuthenticationEntryPoint;
import com.ssafy.mugit.global.security.CustomAuthorizeHttpRequestsFilter;
import com.ssafy.mugit.global.security.CustomUserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDenialHandler customAccessDenialHandler;
    private final CustomUserAuthenticationFilter customUserAuthenticationFilter;
    private final CustomAuthorizeHttpRequestsFilter customAuthorizeHttpRequestsFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                // Authorize Filter
                .authorizeHttpRequests(customAuthorizeHttpRequestsFilter.regist())

                // Redis Authentication Filter
                .addFilterBefore(customUserAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // form login
                .formLogin(Customizer.withDefaults())

                // 401 handler
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler.authenticationEntryPoint(customAuthenticationEntryPoint))

                // 403 handler
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler.accessDeniedHandler(customAccessDenialHandler))

                // CSRF Disable
                .csrf(AbstractHttpConfigurer::disable)

                .build();
    }
}