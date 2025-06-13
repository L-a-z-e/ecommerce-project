package com.laze.ecommerceproject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 헤더에서 'Authorization' 헤더를 찾음
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // 2. 'Authorization' 헤더가 있고, 'Bearer '로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt =authorizationHeader.substring(7); // 'Bearer ' 다음의 토큰 부분만 추출
            email = jwtUtil.getEmailFromToken(jwt);
        }

        // 3. 토큰에서 이메일 성공적으로 추출하고, 아직 SecurityContext에 인증 정보가 없는 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 4. DB에서 사용자 정보를 가져옴
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // 5. 토큰이 유효한지 검증
            if (jwtUtil.validateToken(jwt)) {
                // 6. 토큰이 유효하면, 인증 토큰을 생성하여 SecuirtyContext에 등록함
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList()); // Role은 임시로 비워둠

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // 7. 다음 필터로 요청을 전달함
        filterChain.doFilter(request, response);
    }
}
