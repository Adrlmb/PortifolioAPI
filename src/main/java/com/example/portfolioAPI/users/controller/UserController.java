package com.example.portfolioAPI.users.controller;
import com.example.portfolioAPI.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService service;
    private final UserRepository repository;

    @GetMapping
    public List<UserDTO> listAll(){
        return service.listAll();
    }

    @PostMapping
    public void insert(@RequestBody UserDTO dto){
        service.insert(dto);
    }

    //add status HTTP with ResponseEntity, validations and exceptions.
}
