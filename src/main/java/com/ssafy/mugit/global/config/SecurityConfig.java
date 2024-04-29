package com.ssafy.mugit.global.config;

import com.ssafy.mugit.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthorizeHttpRequestsFilter customAuthorizeHttpRequestsFilter;
    private final CustomOncePerRequestFilter customOncePerRequestFilter;
    private final CustomAdminLoginHandler customAdminLoginHandler;
    private final CustomAdminLoginFailureHandler customAdminLoginFailureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDenialHandler customAccessDenialHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                // Authorize Filter
                .authorizeHttpRequests(customAuthorizeHttpRequestsFilter.regist())

                // Make Authentication Object by Redis Session
                .addFilterBefore(customOncePerRequestFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // form login
                .formLogin((loginConfigurer) ->{
                            loginConfigurer.successHandler(customAdminLoginHandler);
                            loginConfigurer.failureHandler(customAdminLoginFailureHandler);
                        }
                    )

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