package com.github.BlackThornLabs.services;

import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.model.User;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.save(user);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String name, String email, int age) {
        validateUserData(name, email, age);
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    private void validateUserData(String name, String email, int age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Недопустимый email");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Возраст не может быть меньше нуля");
        }
    }
}