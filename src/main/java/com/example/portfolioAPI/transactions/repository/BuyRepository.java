package com.example.portfolioAPI.transactions.repository;

import com.example.portfolioAPI.transactions.entity.BuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface BuyRepository extends JpaRepository<BuyEntity, Long> {
    @Query("SELECT SUM(b.profit) FROM BuyEntity b")
    BigDecimal sumProfit();

    @Query("SELECT AVG(b.cryptoValue) FROM BuyEntity b")
    BigDecimal average();
}
