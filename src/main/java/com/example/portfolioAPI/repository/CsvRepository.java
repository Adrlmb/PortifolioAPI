package com.example.portfolioAPI.repository;

import com.example.portfolioAPI.entity.CsvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvRepository extends JpaRepository<CsvEntity, Long> {
}
