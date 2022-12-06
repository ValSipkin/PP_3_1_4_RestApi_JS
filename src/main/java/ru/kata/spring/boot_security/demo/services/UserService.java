package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findById(int id);
    List<User> findAll();
    void saveUser(User user);
    void deleteUser(User user);
}
