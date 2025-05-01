package com.example.portfolioAPI.dto;

import com.example.portfolioAPI.entity.BuyEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@NoArgsConstructor // because already have a constructor created
@Data
public class BuyDTO {
    private Long id;
    private String code;
    private String codein; // crypto used to buy
    private BigDecimal bid;
    private String buyDate;
    private String amountCryptoPurchased;
    private BigDecimal cryptoValue;
    private BigDecimal amountSpent; // amount used to buy crypto
    private String taxCryptoCode;
    private BigDecimal taxAmount;
    private String exchange;
    private BigDecimal profit;
    private BigDecimal totalProfit;
    private BigDecimal averageValue;

    public BuyDTO(BuyEntity entity){
        BeanUtils.copyProperties(entity, this);
    }

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    public void setCodein(String codein) {
        this.codein = codein.toUpperCase();
    }

    public void setTaxCryptoCode(String taxCryptoCode) {
        this.taxCryptoCode = taxCryptoCode.toUpperCase();
    }
    public void setExchange(String exchange){
        this.exchange = (exchange != null) ? exchange.toUpperCase() : null;
    }
}
