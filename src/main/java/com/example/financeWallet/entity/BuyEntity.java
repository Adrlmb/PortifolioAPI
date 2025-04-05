package com.example.financeWallet.entity;

import com.example.financeWallet.dto.BuyDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
@NoArgsConstructor
@AllArgsConstructor
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
    private String bid;

    @Column(name = "BUY_DATE")
    private String buyDate;

    @Column(name = "AMOUNT_PURCHASED")
    private String amountCryptoPurchased;

    @Column(name = "AMOUNT_SPENT")
    private String amountSpent; // amount used to buy crypto

    @Column(name = "TAX_CODE")
    private String taxCryptoCode;

    @Column(name = "TAX_AMOUNT")
    private String taxAmount;

    @Column(name = "EXCHANGE")
    private String exchange;

    @Column(name = "profit")
    private Long profit;

    public BuyEntity(BuyDTO dto){
        BeanUtils.copyProperties(dto, this);
    }

}
