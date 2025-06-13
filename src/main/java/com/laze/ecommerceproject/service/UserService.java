package com.laze.ecommerceproject.service;

import com.laze.ecommerceproject.config.JwtUtil;
import com.laze.ecommerceproject.controller.dto.LoginRequest;
import com.laze.ecommerceproject.controller.dto.LoginResponse;
import com.laze.ecommerceproject.controller.dto.SignUpRequest;
import com.laze.ecommerceproject.domain.User;
import com.laze.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

// 이 클래스가 비즈니스 로직을 처리하는 서비스 계층임을 나타냄
@Service
// 클래스 전체에 트랜잭션 적용 => 기본적으로 모든 메소드를 읽기 전용으로 설정하여 성능 최적화
@Transactional(readOnly = true)
// final 필드에 대한 생성자를 자동으로 만들어주는 Lombok annotation (DI)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    // @RequiredArgsConstructor가 생성자를 만들어주기 때문에 final 키워드만 붙이면 됨
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 로직
    // @Transactional : 이 메소드는 데이터베이스에 변경을 가하기 때문에 클래스 레벨의 readOnly 설정을 덮어쓰고 일반 트랜잭션 설정 적용
    @Transactional
    public void signUp(SignUpRequest request) {
        // 1. 중복 이메일 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. DTO를 Entity로 변환하여 DB에 저장
        User user = new User(request.getUsername(), encodedPassword, request.getEmail());
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        // 1. 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 로그인 성공 (JWT 생성 => 반환)
        String accessToken = jwtUtil.createAccessToken(user.getEmail());

        return new LoginResponse(accessToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일을 사용하여 DB에서 User 엔티티를 찾음
        return userRepository.findByEmail(email)
                // User 엔티티를 Spring Security의 UserDetails 객체로 변환
        // 첫 번째 인자: username
        // 두 번째 인자: password
        // 세 번째 인자: authorities
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.emptyList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. email: " + email));
    }
}