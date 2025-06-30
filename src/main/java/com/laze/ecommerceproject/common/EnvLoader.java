package com.laze.ecommerceproject.common;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    static {
        // Spring Profile 확인
        String profile = System.getProperty("spring.profiles.active", "default");

        // 환경에 따라 파일명 설정
        String envFile = switch (profile) {
            case "prod" -> ".env-prod";
            case "dev" -> ".env-dev";
            default -> ".env";
        };

        // dotenv 로딩
        Dotenv dotenv = Dotenv.configure()
                .filename(envFile)
                .ignoreIfMissing()  // 파일 없으면 예외 없이 무시
                .load();

        // 시스템 속성에 설정값 적용
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()));
    }
}
