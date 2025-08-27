package com.github.BlackThornLabs.controller;

import com.github.BlackThornLabs.dto.UserRequestDTO;
import com.github.BlackThornLabs.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users", String.class);
        assertAll(
                ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()-> assertThat(response.getBody()).contains("id", "name", "email")
        );
    }

    @Test
    void shouldReturnUserById() {
        ResponseEntity<UserResponseDTO> response = restTemplate.getForEntity("/api/users/1", UserResponseDTO.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(1L, response.getBody().getId())
        );
    }

    @Test
    void shouldCreateUser() {
        // Генерируем уникальный email для каждого теста
        String uniqueEmail = "test_" + System.currentTimeMillis() + "@mail.com";

        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName("Test User");
        newUser.setEmail(uniqueEmail);
        newUser.setAge(25);

        ResponseEntity<UserResponseDTO> response = restTemplate.postForEntity("/api/users", newUser, UserResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
