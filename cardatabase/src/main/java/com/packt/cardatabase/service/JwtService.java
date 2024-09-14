package com.packt.cardatabase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// 서명된 JWT를 생성하고 검증하는 클래스
// EXPIRATIONTIME은 토큰의 만료 시간이고 PREFIX는 토큰의 접두사를 정의하며,
// 일반적으로 Bearer 스키마를 이용한다.
// 비밀 키는 jjwt 라이브러리의 secretKeyFor 메서드로 생성한다.
// getToken 메서드는 토큰을 생성하고 반환한다.
// getAuthUser 메서드는 응답의 Authorization 헤더에서 토큰을 가져온다.
// 그 다음에는 jjwt 라이브러리의 parseBuilder 메서드를 이용해 JwtParserBuilder
// 인스턴스를 생성한다. setSigningKey 메서드는 토큰 검증을 위한 비밀 키를 지정한다.
@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000; // 1을 밀리초로 계산한 값
    static final String PREFIX = "Bearer";
    // 비밀 키 생성. 시연 용도로만 이용해야 함
    // 운영단계에서는 애플리케이션 구성에서 읽을 수 있고 읽어야만 함
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 서명된 JWT 토큰 생성
    public String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()
                    + EXPIRATIONTIME))
                .signWith(key)
                .compact();

        return token;
    }

    // 요청 권한 부여 헤더에서 토큰을 가져와
    // 토큰을 확인하고 사용자 이름을 얻음
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null) {
            String user = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null)
                return user;
        }
        return null;
    }
}
