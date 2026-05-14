package com.example.Todo.web;


import com.example.Todo.entities.User;
import com.example.Todo.repositories.UserRepository;
import com.example.Todo.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.
        AuthenticationManager;

import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager
            authenticationManager;

    public AuthController(
            UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
        this.authenticationManager =
                authenticationManager;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("username is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        String token =
                jwtService.generateToken(user.getUsername());

        return Map.of("token", token);
    }
}