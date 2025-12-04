package com.example.portfolioAPI.users.dto;

import com.example.portfolioAPI.users.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;

public UserDTO(UserEntity entity){
    BeanUtils.copyProperties(entity, this);
}

}
