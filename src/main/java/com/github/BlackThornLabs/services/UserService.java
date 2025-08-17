package com.github.BlackThornLabs.services;

import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.model.User;
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
}