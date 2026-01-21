package com.slog.blog_api.global.config;

import com.slog.blog_api.global.auth.handler.OAuth2SuccessHandler;
import com.slog.blog_api.global.auth.service.CustomOAuth2UserService;
import com.slog.blog_api.global.jwt.JwtAuthenticationFilter;
import com.slog.blog_api.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 1. 권한 설정 (순서가 생명!)
                .authorizeHttpRequests(auth -> auth
                        // (A) [허용] 정적 리소스 및 로그인 관련 URL
                        .requestMatchers("/oauth2/**", "/login/**", "/error", "/favicon.ico").permitAll()

                        // (B) [허용] 게시글/카테고리 "조회(GET)" 만 허용! (POST, PUT, DELETE는 안 됨)
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**", "/api/v1/categories/**").permitAll()

                        // (C) [차단] 나머지는 전부 인증 필요 (글쓰기 등)
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        // 인증되지 않은 사용자 -> 401 Unauthorized 에러 반환
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                // 2. OAuth2 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                // 3. 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
