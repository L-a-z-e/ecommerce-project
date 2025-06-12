package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST API를 처리하는 Controller임을 나타냄
// 각 메소드의 반환 값은 자동으로 JSON 형태로 변환됨
@RestController
// 이 컨트롤러의 모든 API는 공통적으로 /api/users 경로를 가짐
@RequestMapping("/api/users")
public class UserController {

    // HTTP POST 요청을 /api/users/signup 경로로 매핑함
    @PostMapping("/signup")
    // @RequestBody : 요청의 body에 담긴 JSON 데이터를 SignUpRequest DTO 객체로 변환해줌
    // @Valid : SignUpRequest DTO에 정의된 유효성 검증 수행
    // 검증 실패시 400 Bad Request 에러 발생 시킴
    public String signUp(@Valid @RequestBody SignUpRequest request) {
        System.out.println("회원가입 요청 받음: " + request.getEmail());
        return "회원가입 성공!";
    }
}
