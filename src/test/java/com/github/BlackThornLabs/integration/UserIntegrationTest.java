package com.github.BlackThornLabs.integration;

import com.github.BlackThornLabs.AbstractIntegrationTest;
import com.github.BlackThornLabs.dto.UserRequest;
import com.github.BlackThornLabs.dto.UserResponse;
import com.github.BlackThornLabs.service.UserService;
import com.github.BlackThornLabs.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateFindAndDeleteUserWithRealPostgreSQL() {
        UserRequest request = TestDataUtil.createUserRequest(
                "Testcontainers Test", "testcontainers@test.com", 35
        );

        UserResponse createdUser = userService.createUser(request);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();

        Optional<UserResponse> foundUser = userService.getUserById(createdUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testcontainers@test.com");
        assertThat(foundUser.get().getName()).isEqualTo("Testcontainers Test");

        boolean deleted = userService.deleteUser(createdUser.getId());
        assertThat(deleted).isTrue();

        Optional<UserResponse> deletedUser = userService.getUserById(createdUser.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldHandlePartialUpdateWithNullFields() {
        UserRequest user1 = TestDataUtil.createUserRequest("User One", "one@test.com", 25);
        UserResponse created1 = userService.createUser(user1);

        UserRequest partialUpdate = TestDataUtil.createUserRequest("Updated Name", null, 0);
        Optional<UserResponse> updated = userService.updateUser(created1.getId(), partialUpdate);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated Name");
        assertThat(updated.get().getEmail()).isEqualTo("one@test.com"); // unchanged
        assertThat(updated.get().getAge()).isEqualTo(25); // unchanged

        userService.deleteUser(created1.getId());
    }
}
