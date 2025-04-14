package com.example.portfolioAPI.entity;

import com.example.portfolioAPI.dto.BuyDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Entity
@Table(name = "FNW_BUY")
public class BuyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CRYPTO_CODE") // crypto bought
    private String code;

    @Column(name = "CRYPTO") // crypto used to buy
    private String codein;

    @Column(name = "CURRENT_VALUE") // crypto used to buy
    private BigDecimal bid;

    @Column(name = "BUY_DATE")
    private String buyDate;

    @Column(name = "AMOUNT_PURCHASED")
    private String amountCryptoPurchased;

    @Column(name = "AMOUNT_SPENT")
    private BigDecimal amountSpent; // amount used to buy crypto

    @Column(name = "CRYPTO_VALUE")
    private BigDecimal cryptoValue;

    @Column(name = "TAX_CODE")
    private String taxCryptoCode;

    @Column(name = "TAX_AMOUNT")
    private BigDecimal taxAmount;

    @Column(name = "EXCHANGE")
    private String exchange;

    @Column(name = "PROFIT")
    private BigDecimal profit;

    @Column(name = "TOTAL_PROFIT")
    private BigDecimal totalProfit;

    @Column(name = "AVERAGE_VALUE")
    private BigDecimal averageValue;

    public BuyEntity(BuyDTO dto){
        BeanUtils.copyProperties(dto, this);
    }

}
