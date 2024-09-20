package com.packt.cardatabase;

// 이전 commit에서 로그인 단계가 완료되었고 이제 나머지 수신 요청에서 인증을
// 처리하는 단계다. 이 작업은 요청 프로세스에서 필터를 이용해 요청이 컨트롤러로
// 전달되기 전이나 응답이 클라이언트로 전송되기 전에 수행 할 수 있다.
// 이제 인증 프로세스의 나머지 과정이다.

import com.packt.cardatabase.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 필터 클래스는 들어오는 모든 요청을 인증하는 데 이용된다.
// AuthenticationFilter 클래스는 스프링 시큐리티의 OncePerRequestFilter
// 인터페이스를 확장한다. 이 인터페이스에는 인증을 구현할 doFilterInternal 메서드가
// 있다. 요청에 있는 토큰을 확인하려면 필요하므로 JwtService 인스턴스를 필터 클래스에
// 주입해야 한다.
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        // Authorization 헤더에서 토큰을 가져옴
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jws != null) {
            // 토큰을 확인하고 사용자를 얻음
            String user = jwtService.getAuthUser(request);
            // 인증
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user, null,
                            java.util.Collections.emptyList());

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
