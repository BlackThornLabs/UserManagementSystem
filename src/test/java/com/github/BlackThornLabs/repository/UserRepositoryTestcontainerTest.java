package com.github.BlackThornLabs.repository;

import com.github.BlackThornLabs.AbstractIntegrationTest;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTestcontainerTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldWorkWithRealPostgreSQL() {
        User user = TestDataUtil.createTestUserA();

        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("java@rules.com");
        assertThat(foundUser.get().getName()).isEqualTo("James Gosling");
    }

    @Test
    void shouldFindByEmailWithRealDatabase() {
        User user = TestDataUtil.createTestUserB();
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("ada@forever.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Ada Lovelace");
    }

    @Test
    void shouldReturnEmptyForNonExistentEmail() {
        Optional<User> result = userRepository.findByEmail("nonexistent@email.com");

        assertThat(result).isEmpty();
    }
}
