package com.packt.cardatabase;

import com.packt.cardatabase.service.UserDetailsServiceImpl;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// 보안 구성 클래스
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 필터 클래스를 스프링 시큐리티 구성에 추가
    @Autowired
    private AuthenticationFilter authenticationFilter;

    // 예외 처리를 위한 AuthEntryPoint 클래스 주입
    @Autowired
    private AuthEntryPoint exceptionHandler;

    // userDetails 메서드를 삭제해서 인메모리 사용자를 비활성화한다.
    // 데이터베이스에서 사용자를 활성화하기 위해 configureGlobal 메서드를 추가해야 한다.
    // 일반 텍스트로 저장할 수 없기 때문에 configureGlobal 메서드에 암호 해싱 알고리즘을 정의해야 한다.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    // AuthenticationManager를 LoginController 클래스에 주입했으므로 bean으로 등록
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 교차 출처 리소르 공유를 위한 CORS 필터
    // 이는 다른 출처에서 요청을 보내는 프런트엔드에 필요하다.
    // CORS 필터는 요청을 가로채고 해당 요청이 교차 출처에서 확인되면
    // 적절한 헤더를 요청에 추가한다. 이를 위해 스프링 시큐리티의
    // CorsConfigurationSource 인터페이스를 이용한다.
    // (모든 출처의 HTTP 방식과 헤더 허용)
    // 클래스에 전역 CORS 필터 추가
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 보호되는 경로와 그렇지 않은 경로를 정의한다.
    // 이 메서드에서 /login 엔드포인트에 대한 POST 요청은 인증 없이도 허용되지만
    // 다른 모든 엔드포인트에 대한 POST 요청은 인증이 필요하도록 지정한다.
    // 또한 스프링 시큐리티가 세션을 생성하지 않도록 정의하므로 csrf를 비활성화할 수 있다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                        // /login 엔드포인트에 대한 POST 요청은 보호되지 않음
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 다른 모든 요청은 보호됨
                        .anyRequest().authenticated()
                )
                // 예외 처리
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(exceptionHandler))
                // /login 엔드포인트를 호출하여 로그인할 수 있으며, 성공하면
                // Authorization 헤더로 JWT를 받는다.
                // 정상적으로 로그인 후 Authorization 헤더로 받은 JWT를 다른
                // RESTFUL 서비스 엔드포인트를 호출하는 데 활용할 수 있다.
                // 예: GET /api/cars Authorization 헤더 : JWT 값
                .addFilterBefore(authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /* deprecated

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    }

    // SecurityConfig 클래스에 userDetailsService() 메서드를 추가하여
    // 메모리 사용자를 애리케이션에 추가할 수도 있다.
    // 이름이 user이고 암호가 password인 인메모리 사용자를 생성한다.
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // 개발 과정 중에는 인메모리 사용자를 이용해도 되지만 실제 애플리케이션에서는
        // 사용자를 데이터베이스에 저장해야 한다. 사용자 엔티티 클래스와 리포지터리를
        // 작성하고 암호는 데이터베이스에 일반 텍스트 형식이 아닌 해싱 알고리즘을
        // 이용한다 (예로, bcrypt)
        UserDetails user =
                // withDefaultPasswordEncoder()는 시연목적에만 적합하며 운영 단계에는
                // 안전 문제로 적합하지 않다.
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
    */
}
