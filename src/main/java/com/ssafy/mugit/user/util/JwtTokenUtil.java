package com.ssafy.mugit.user.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${cookie.domain}")
    private String domainUrl;

    @Value("${jwt.period.access-token}")
    private long periodAccessToken;

    @Value("${jwt.period.refresh-token}")
    private long periodRefreshToken;

    public String generateAccessToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(Jwts.claims().setId(id.toString()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + periodAccessToken))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(Jwts.claims().setId(id.toString()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + periodRefreshToken))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰 형식");
            return false;
        } catch (MalformedJwtException e) {
            log.error("위조된 토큰");
            return false;
        } catch (SignatureException e) {
            log.error("토큰 키 오류");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("잘못된 형식");
            return false;
        }
    }

    public Long getIdFromToken(String token) {
        Claims claims = (Claims) getClaimsFromToken(token).get("id");
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
