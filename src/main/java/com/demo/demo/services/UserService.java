package com.demo.demo.services;

import com.demo.demo.entities.UserClient;

import java.util.Optional;

public interface UserService {
    String greeting();
    Iterable<UserClient> getAllUsers();
    Optional<UserClient> getUserById(String id);
    String addUser(UserClient user);
    String updateUser(UserClient user);
    String deleteUser(UserClient user);
    String addAllUsers(Iterable<UserClient> users);
    String deleteUsers(Iterable<UserClient> users);
}
