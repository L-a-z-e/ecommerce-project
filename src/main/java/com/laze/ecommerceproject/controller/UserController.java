package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.LoginRequest;
import com.laze.ecommerceproject.controller.dto.LoginResponse;
import com.laze.ecommerceproject.controller.dto.SignUpRequest;
import com.laze.ecommerceproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// REST API를 처리하는 Controller임을 나타냄
// 각 메소드의 반환 값은 자동으로 JSON 형태로 변환됨
@RestController
@RequiredArgsConstructor
// 이 컨트롤러의 모든 API는 공통적으로 /api/users 경로를 가짐
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // HTTP POST 요청을 /api/users/signup 경로로 매핑함
    @PostMapping("/signup")
    // @RequestBody : 요청의 body에 담긴 JSON 데이터를 SignUpRequest DTO 객체로 변환해줌
    // @Valid : SignUpRequest DTO에 정의된 유효성 검증 수행
    // 검증 실패시 400 Bad Request 에러 발생 시킴
    public String signUp(@Valid @RequestBody SignUpRequest request) {

        userService.signUp(request);

        return "회원가입 성공!";
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public String getMyInfo() {
        return "내 정보 조회 성공";
    }
}
