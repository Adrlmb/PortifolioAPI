package com.example.portfolioAPI.transactions.repository;

import com.example.portfolioAPI.transactions.entity.BuyEntity;
import com.example.portfolioAPI.users.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BuyRepository extends JpaRepository<BuyEntity, Long> {
    @Query("SELECT SUM(b.profit) FROM BuyEntity b")
    BigDecimal sumProfit();

    @Query("SELECT AVG(b.cryptoValue) FROM BuyEntity b")
    BigDecimal average();

    List<BuyEntity> findAllByUser (UserEntity user);

    Optional<BuyEntity> findByIdAndUser(Long id, UserEntity user);
}
