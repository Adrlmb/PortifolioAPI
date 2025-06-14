package com.example.portfolioAPI.users.entity;

import com.example.portfolioAPI.users.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USER_TABLE")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String hashSenha;

    public UserEntity(UserDTO dto){
        BeanUtils.copyProperties(dto, this);
    }
}
