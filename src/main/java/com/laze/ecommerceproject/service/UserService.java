package com.laze.ecommerceproject.service;

import com.laze.ecommerceproject.controller.dto.SignUpRequest;
import com.laze.ecommerceproject.domain.User;
import com.laze.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 이 클래스가 비즈니스 로직을 처리하는 서비스 계층임을 나타냄
@Service
// 클래스 전체에 트랜잭션 적용 => 기본적으로 모든 메소드를 읽기 전용으로 설정하여 성능 최적화
@Transactional(readOnly = true)
// final 필드에 대한 생성자를 자동으로 만들어주는 Lombok annotation (DI)
@RequiredArgsConstructor
public class UserService {

    // @RequiredArgsConstructor가 생성자를 만들어주기 때문에 final 키워드만 붙이면 됨
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


}
