package com.laze.ecommerceproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //모든 경로에 대해
                .allowedOrigins("http://localhost:8081") // http://localhost:8081 출처의 요청 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP METHOD
                .allowedHeaders("*") // 모든 헤더를 허용
                .allowCredentials(true) // 쿠키/인증 정보 전송을 허용
                .maxAge(3600); // 프리플라이트 요청의 캐시 시간
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
