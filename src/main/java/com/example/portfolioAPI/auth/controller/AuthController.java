package com.example.portfolioAPI.auth.controller;

import com.example.portfolioAPI.auth.dto.LoginDTO;
import com.example.portfolioAPI.auth.dto.TokenResponse;
import com.example.portfolioAPI.auth.jwt.JwtService;
import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.example.portfolioAPI.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    UserRepository repository;
    JwtService jwtService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        UserEntity entity = new UserEntity(user);
        repository.save(entity);
        return ResponseEntity.ok("User Created");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO login){
        UserEntity user = repository.findByEmail(login.getEmail()).orElseThrow(()-> new RuntimeException("Invalid password"));

        if(!new BCryptPasswordEncoder().matches(login.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
