package com.github.BlackThornLabs.service;

import com.github.BlackThornLabs.dto.UserRequest;
import com.github.BlackThornLabs.dto.UserResponse;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    //Маппер из User в DTO
    private UserResponse toResponse(User user) {
        System.out.println("DEBUG toResponse: user.name='" + user.getName() + "', user.email='" + user.getEmail() + "'");
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setCreatedAt(user.getCreatedAt());
        System.out.println("DEBUG toResponse: response.name='" + response.getName() + "', response.email='" + response.getEmail() + "'");
        return response;
    }

    //Маппер из DTO в User
    private User toEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
    }

    // Создание записи пользователя через DTO
    public UserResponse createUser(@Valid UserRequest request) {
        User user = toEntity(request);
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    // Чтение записей из базы
    public Optional<UserResponse> getUserById(long id) {
        return userRepository.findById(id).map(this::toResponse);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toResponse);
    }

    // Обновление записи пользователя
    public Optional<UserResponse> updateUser(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    request.applyToUser(user); // Используем безопасный метод
                    User updatedUser = userRepository.save(user);
                    return toResponse(updatedUser);
                });
    }

    // Удаление записи
    public boolean deleteUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else return false;
    }
}
