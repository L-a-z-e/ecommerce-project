package com.laze.ecommerceproject.repository;

import com.laze.ecommerceproject.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// 이 클래스가 Spring Boot 환경에서 실행되는 테스트 클래스임을 나타냄
@SpringBootTest
// 테스트가 끝난 후, 데이터베이스의 변경사항을 롤백
// 각 테스트가 서로에게 영향을 주지 않도록 보장하는 역할
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testingCreateUserFindUser() {
        // given : 해당 데이터가 주어졌을 때
        User newUser = new User("testuser", "testpassword", "test@email.com");

        // when : 해당 작업을 수행하면
        User savedUser = userRepository.save(newUser);
        User foundUser = userRepository.findById(savedUser.getId()).orElseThrow();

        // then : 다음 결과가 나와야 함
        assertThat(foundUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(foundUser.getUsername()).isEqualTo(newUser.getUsername());
    }

    @Test
    void testingFindByEmail() {
        // given
        User newUser = new User("finduser", "findpassword", "find@email.com");
        userRepository.save(newUser);

        // when
        User foundUser = userRepository.findByEmail(newUser.getEmail()).orElseThrow();

        // then
        assertThat(foundUser.getEmail()).isEqualTo(newUser.getEmail());
    }
}
