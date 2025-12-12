package com.example.portfolioAPI.auth.controller;

import com.example.portfolioAPI.auth.dto.LoginDTO;
import com.example.portfolioAPI.auth.dto.TokenResponse;
import com.example.portfolioAPI.auth.jwt.JwtService;
import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.example.portfolioAPI.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO user) {
        user.setPassword(encoder.encode(user.getPassword()));
        UserEntity entity = new UserEntity(user);
        repository.save(entity);
        return ResponseEntity.ok("User Created");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        UserEntity user = repository.findByEmail(login.getEmail()).orElseThrow(() -> new RuntimeException("Invalid password"));

        if (!encoder.matches(login.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or p  assword");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
