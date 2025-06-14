package com.example.portfolioAPI.transactions.repository;

import com.example.portfolioAPI.transactions.entity.CsvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvRepository extends JpaRepository<CsvEntity, Long> {
}
