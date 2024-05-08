package com.ssafy.mugit.flow.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class FlowCookieUtil {
    @Value("${cookie.domain}")
    private String domainUrl;

    public boolean firstRead(HttpServletRequest request, HttpServletResponse response, Long flowId) {
        Cookie[] cookies = request.getCookies();
        Cookie oldCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visitedFlows")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie == null) {
            ResponseCookie newCookie = ResponseCookie.from("visitedFlows", "[" + flowId.toString() + "]")
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .maxAge(60 * 60 * 24)
                    .domain(domainUrl)
                    .build();
            response.addHeader("Set-Cookie", newCookie.toString());
            return true;
        } else {
            if (!oldCookie.getValue().contains("[" + flowId.toString() + "]")) {
                ResponseCookie newCookie = ResponseCookie.from("visitedFlows", oldCookie.getValue() + "[" + flowId + "]")
                        .secure(true)
                        .path("/")
                        .sameSite("None")
                        .maxAge(60 * 60 * 24)
                        .domain(domainUrl)
                        .build();
                response.addHeader("Set-Cookie", newCookie.toString());
                return true;
            }
            return false;
        }
    }

}
