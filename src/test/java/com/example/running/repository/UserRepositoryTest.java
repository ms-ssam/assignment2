package com.example.running.repository;

import com.example.running.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional()
    public void userTest() {
        User user = User.builder().id(1L).build();

        userRepository.save(user);

        System.out.println("방금 생성한 사용자의 ID : " + user.getId());

        em.flush();
        em.clear();

        Optional<User> findUserOptional = userRepository.findById(user.getId());

        if(findUserOptional.isEmpty()) {
            throw new IllegalArgumentException("DB에 존재하지 않는 사용자");
        }

        User findUser = findUserOptional.get();

        Assertions.assertNotNull(findUser);
    }

}