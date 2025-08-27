package com.github.BlackThornLabs.service;

import com.github.BlackThornLabs.dto.UserRequestDTO;
import com.github.BlackThornLabs.dto.UserResponseDTO;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Test User");
        request.setEmail("test@email.com");
        request.setAge(25);

        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Test User");
        assertThat(response.getEmail()).isEqualTo("test@email.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldReturnUserById() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyForNonExistentUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserById(999L);

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User existingUser = User.builder()
                .id(1L)
                .name("Old Name")
                .email("old@email.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        UserRequestDTO request = new UserRequestDTO();
        request.setName("New Name");
        request.setEmail("new@email.com");
        request.setAge(30);

        User updatedUser = User.builder()
                .id(1L)
                .name("New Name")
                .email("new@email.com")
                .age(30)
                .createdAt(existingUser.getCreatedAt())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<UserResponseDTO> result = userService.updateUser(1L, request);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Name");
        assertThat(result.get().getEmail()).isEqualTo("new@email.com");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Given
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);  // ← ДОБАВИТЬ ЭТО!
        doNothing().when(userRepository).deleteById(1L);      // ← Или delete(any(User.class))

        boolean result = userService.deleteUser(1L);

        assertThat(result).isTrue();
        verify(userRepository, times(1)).existsById(1L);      // ← ПРОВЕРИТЬ ЭТОТ ВЫЗОВ
        verify(userRepository, times(1)).deleteById(1L);      // ← И ЭТОТ
    }

}
