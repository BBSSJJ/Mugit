package com.ssafy.mugit.global.config;

import com.ssafy.mugit.global.security.CustomAccessDenialHandler;
import com.ssafy.mugit.global.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDenialHandler customAccessDenialHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll())

                // 401 handler
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler.accessDeniedHandler(customAccessDenialHandler))

                // 403 handler
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler.authenticationEntryPoint(customAuthenticationEntryPoint))

                // CSRF Disable
                .csrf(AbstractHttpConfigurer::disable)

                .build();
    }
}