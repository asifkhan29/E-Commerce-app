package com.example.ecommerce_app.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_app.entity.User;
import com.example.ecommerce_app.repository.UserRepository;
import com.example.ecommerce_app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    
	    @Transactional
	    public User createUser(User user) {
	        log.info("Creating new user: {}", user.getUsername());
	        
	        // Encode password
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        
	        return userRepository.save(user);
	    }
	    
	    @Cacheable(value = "users", key = "#id")
	    public User getUserById(Long id) {
	        log.debug("Fetching user with id: {}", id);
	        return userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	    }
	    
//	    @CacheEvict(value = "users", key = "#id")
	    @Transactional
	    public User updateUserRole(Long id, User.UserRole role) {
	        User user = getUserById(id);
	        user.setRole(role);
	        log.info("Updated user {} role to {}", user.getUsername(), role);
	        return userRepository.save(user);
	    }
	    
//	    @Cacheable(value = "usersByPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	    public Page<User> getAllUsers(Pageable pageable) {
	        log.debug("Fetching all users datas with pagination");
	        return userRepository.findAll(pageable);
	    }
	    
	    public User getUserByUsername(String username) {
	        return userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found: " + username));
	    }}


