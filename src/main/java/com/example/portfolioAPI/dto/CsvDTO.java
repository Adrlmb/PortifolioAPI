package com.example.portfolioAPI.dto;

import com.example.portfolioAPI.entity.CsvEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class CsvDTO {
    private Long id;
    private String code;
    private String codein; // crypto used to buy
    private BigDecimal bid;
    private String buyDate;
    private String amountCryptoPurchased;
    private String cryptoValue;
    private BigDecimal amountSpent; // amount used to buy crypto
    private String taxCryptoCode;
    private BigDecimal taxAmount;
    private String exchange;
    private BigDecimal profit;
    private BigDecimal totalProfit;
    private BigDecimal averageValue;

    public CsvDTO(CsvEntity csvEntity){
        BeanUtils.copyProperties(csvEntity, this);
    }
}
