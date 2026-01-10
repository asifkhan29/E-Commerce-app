package com.example.ecommerce_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_app.dto.AuthRequest;
import com.example.ecommerce_app.dto.AuthResponse;
import com.example.ecommerce_app.dto.RegisterRequest;
import com.example.ecommerce_app.entity.User;
import com.example.ecommerce_app.security.CustomUserDetails;
import com.example.ecommerce_app.security.JwtService;
import com.example.ecommerce_app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication , Login/Register User , Admin , Premium_User", description = "Authentication APIs")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getId()
        ));
    }

    @PostMapping("/register")
    @Operation(
    	    summary = "Register new user",
    	    description = "Register USER, PREMIUM_USER or ADMIN"
    	)
    	@io.swagger.v3.oas.annotations.parameters.RequestBody(
    	    content = @io.swagger.v3.oas.annotations.media.Content(
    	        examples = {

    	            @ExampleObject(
    	                name = "USER",
    	                summary = "Normal User",
    	                value = """
    	                {
    	                  "username": "john_user",
    	                  "email": "john.user@example.com",
    	                  "password": "password123",
    	                  "role": "USER"
    	                }
    	                """
    	            ),

    	            @ExampleObject(
    	                name = "PREMIUM_USER",
    	                summary = "Premium User",
    	                value = """
    	                {
    	                  "username": "alice_premium",
    	                  "email": "alice.premium@example.com",
    	                  "password": "password123",
    	                  "role": "PREMIUM_USER"
    	                }
    	                """
    	            ),

    	            @ExampleObject(
    	                name = "ADMIN",
    	                summary = "Admin User",
    	                value = """
    	                {
    	                  "username": "admin_root",
    	                  "email": "admin@example.com",
    	                  "password": "admin123",
    	                  "role": "ADMIN"
    	                }
    	                """
    	            )
    	        }
    	    )
    	)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    	 log.info("Registering new user: {}", request.getUsername());

    	    User user = new User();
    	    user.setUsername(request.getUsername());
    	    user.setEmail(request.getEmail());
    	    user.setPassword(request.getPassword());
    	    user.setRole(request.getRole()); 

    	    User savedUser = userService.createUser(user);
    	    String jwt = jwtService.generateToken(savedUser);

    	    return ResponseEntity.ok(new AuthResponse(
    	            jwt,
    	            savedUser.getUsername(),
    	            savedUser.getEmail(),
    	            savedUser.getRole(),
    	            savedUser.getId()
    	    ));
    }
}