package com.github.BlackThornLabs.integration;

import com.github.BlackThornLabs.AbstractIntegrationTest;
import com.github.BlackThornLabs.dto.UserRequestDTO;
import com.github.BlackThornLabs.dto.UserResponseDTO;
import com.github.BlackThornLabs.service.UserService;
import com.github.BlackThornLabs.util.TestDataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class UserIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateFindAndDeleteUserWithRealPostgreSQL() {
        UserRequestDTO request = TestDataUtil.createUserRequest(
                "Testcontainers Test", "testcontainers@test.com", 35
        );

        UserResponseDTO createdUser = userService.createUser(request);
        assertAll(
                ()-> assertThat(createdUser).isNotNull(),
                ()-> {
                    Assertions.assertNotNull(createdUser);
                }
        );



        Optional<UserResponseDTO> foundUser = userService.getUserById(createdUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testcontainers@test.com");
        assertThat(foundUser.get().getName()).isEqualTo("Testcontainers Test");

        boolean deleted = userService.deleteUser(createdUser.getId());
        assertThat(deleted).isTrue();

        Optional<UserResponseDTO> deletedUser = userService.getUserById(createdUser.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldHandlePartialUpdateWithNullFields() {
        UserRequestDTO user1 = TestDataUtil.createUserRequest("User One", "one@test.com", 25);
        UserResponseDTO created1 = userService.createUser(user1);

        UserRequestDTO partialUpdate = TestDataUtil.createUserRequest("Updated Name", null, 0);
        Optional<UserResponseDTO> updated = userService.updateUser(created1.getId(), partialUpdate);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated Name");
        assertThat(updated.get().getEmail()).isEqualTo("one@test.com"); // unchanged
        assertThat(updated.get().getAge()).isEqualTo(25); // unchanged

        userService.deleteUser(created1.getId());
    }
}
