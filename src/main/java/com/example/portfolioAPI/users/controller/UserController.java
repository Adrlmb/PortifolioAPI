package com.example.portfolioAPI.users.controller;

import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService service;

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
