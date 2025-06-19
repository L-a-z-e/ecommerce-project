package com.laze.ecommerceproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    // Spring Security에서 사용자 정보를 불러오는 표준 인터페이스
    // 직접 구현 또는 기존의 UserService 활용 가능
    // jwt token => 중복 데이터 select 제거
    // private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 헤더에서 'Authorization' 헤더를 찾음
        final String authorizationHeader = request.getHeader("Authorization");

        // 2. 헤더가 없거나 'Bearer '로 시작하지 않으면 다음 필터로 넘김 => 토큰이 없는 요청은 파싱 시도하지 않음
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt =authorizationHeader.substring(7); // 'Bearer ' 다음의 토큰 부분만 추출

        // 3. 토큰이 유효한지 먼저 검사하고, 유효할 때만 이메일, 인증 추출 처리함
        if (jwtUtil.validateToken(jwt)) {
            String email = jwtUtil.getEmailFromToken(jwt);
            String role = jwtUtil.getRoleFromToken(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, Collections.singleton(authority));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 4. 토큰이 유효하지 않더라도 다음 필터로 계속 진행시킴
        filterChain.doFilter(request, response);
    }
}
