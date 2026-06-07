package com.example.Todo.web;


import com.example.Todo.entities.Todo;
import com.example.Todo.entities.User;
import com.example.Todo.repositories.UserRepository;
import com.example.Todo.security.JwtService;

import com.example.Todo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.
        AuthenticationManager;

import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager
            authenticationManager;


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
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
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()));
            if (authentication.isAuthenticated()) {
                //Map<String,Object> authData = new HashMap<>();
                String authData;
                //authData.put("token", jwtService.generateToken(user.getUsername()));
                authData = jwtService.generateToken(user.getUsername());
                //authData.put("type", "Bearer");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}