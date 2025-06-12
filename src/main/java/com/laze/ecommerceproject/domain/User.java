package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID 자동 생성 및 관리 auto-increment
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "address")
    private String address;

    @Column(name = "point")
    private int point = 0; // 기본값 지정

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
