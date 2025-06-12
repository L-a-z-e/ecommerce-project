package com.laze.ecommerceproject.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

    // @NotBlank: Null, "", " " (공백만 있는 문자열)을 모두 허용하지 않음
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String username;
}
