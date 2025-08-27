package com.github.BlackThornLabs.util;

import com.github.BlackThornLabs.dto.UserRequestDTO;
import com.github.BlackThornLabs.model.User;

import java.time.LocalDateTime;

// Класс для создания тестовых данных
public class TestDataUtil {

    // Методы для создания User Entity
    public static User createTestUserA() {
        return User.builder()
                .name("James Gosling")
                .email("java@rules.com")
                .age(70)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User createTestUserB() {
        return User.builder()
                .name("Ada Lovelace")
                .email("ada@forever.com")
                .age(36)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User createTestUserC() {
        return User.builder()
                .name("Alfonso John Romero")
                .email("doom@guy.com")
                .age(32)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Универсальный метод для создания User с кастомными параметрами
    public static User createUser(String name, String email, int age) {
        return User.builder()
                .name(name)
                .email(email)
                .age(age)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Методы для создания UserRequestDTO DTO
    public static UserRequestDTO createUserRequestA() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("James Gosling");
        request.setEmail("java@rules.com");
        request.setAge(70);
        return request;
    }

    public static UserRequestDTO createUserRequestB() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Ada Lovelace");
        request.setEmail("ada@forever.com");
        request.setAge(36);
        return request;
    }

    public static UserRequestDTO createUserRequest(String name, String email, int age) {
        UserRequestDTO request = new UserRequestDTO();
        request.setName(name);
        request.setEmail(email);
        request.setAge(age);
        return request;
    }

    // Метод для создания UserRequestDTO для обновления
    public static UserRequestDTO createUpdateRequest() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Updated Name");
        request.setEmail("updated@email.com");
        request.setAge(40);
        return request;
    }

    public static User createUserWithId(Long id, String name, String email, int age) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .age(age)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
