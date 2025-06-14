package com.example.portfolioAPI.users.dto;

import com.example.portfolioAPI.users.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserDTO {
    private String nome;
    private String email;
    private String hashSenha;

public UserDTO(UserEntity entity){
    BeanUtils.copyProperties(entity, this);
}

}
