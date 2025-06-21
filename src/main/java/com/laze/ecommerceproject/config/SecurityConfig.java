package com.laze.ecommerceproject.config;

import com.laze.ecommerceproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
// Spring Security의 웹 보안 설정 활성화
@EnableWebSecurity
@RequiredArgsConstructor
@Lazy
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // @Bean : 이 메소드가 반환하는 인스턴스를 Spring Container가 관리하도록 등록
    // 다른 곳에서 PasswordEncoder 주입 받아서 사용 가능

    // SecurityFilterChain => HTTP 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Cross-Site RequestForgery 보호 기능 비활성화
        // REST API 서버는 보통 세션 대신 토큰을 사용하므로 CSRF 보호가 필요 없는 경우가 많음
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                // http 요청에 대한 접근 권한 설정
                // antMatchers 는 과거 방식 => Spring Security 5.x 까지 사용됨 패턴(*, **, ?)을 사용하여 URL 경로를 매칭하던 방식
                // requestMatchers => Spring Security 6.x 부터 사용됨 단순 경로 뿐만 아니라 Get, Post, 까지 조합하여 규칙을 만들 수 있음
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/users/signup", "api/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/cart/items/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/cart/items/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}