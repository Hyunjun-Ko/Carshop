package com.packt.cardatabase;

import com.packt.cardatabase.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// 보안 구성 클래스
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // userDetails 메서드를 삭제해서 인메모리 사용자를 비활성화한다.
    // 데이터베이스에서 사용자를 활성화하기 위해 configureGlobal 메서드를 추가해야 한다.
    // 일반 텍스트로 저장할 수 없기 때문에 configureGlobal 메서드에 암호 해싱 알고리즘을 정의해야 한다.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
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
