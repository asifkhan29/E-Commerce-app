package com.example.ecommerce_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_app.entity.User;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User updateUserRole(Long id, User.UserRole role);

    Page<User> getAllUsers(Pageable pageable);

    User getUserByUsername(String username);
}
