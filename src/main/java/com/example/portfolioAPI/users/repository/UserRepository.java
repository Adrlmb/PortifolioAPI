package com.example.portfolioAPI.users.repository;

import com.example.portfolioAPI.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
