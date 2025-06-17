package com.example.portfolioAPI.users.service;

import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.example.portfolioAPI.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDTO> listAll(){
        List<UserEntity> entity = userRepository.findAll();
        return entity.stream().map(UserDTO::new).toList();
    }

    public void insert(UserDTO dto){
        UserEntity entity = new UserEntity(dto);
        userRepository.save(entity);
    }
}
