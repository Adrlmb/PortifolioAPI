package com.example.financeWallet.dto;

import com.example.financeWallet.entity.BuyEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Getter
@Setter
@ToString

public class BuyDTO {
    private Long id;
    private String purchasedCryptoCode;
    private String cryptoUsed; // crypto used to buy
    private String buyDate;
    private String amountCryptoPurchased;
    private String amountSpent; // amount used to buy crypto
    private String taxCryptoCode;
    private String taxAmount;
    private String exchange;
    private String profit;

    public BuyDTO(BuyEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
