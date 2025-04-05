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
    private String code;
    private String codein; // crypto used to buy
    private String bid;
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

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    public void setCodein(String codein) {
        this.codein = codein.toUpperCase();
    }

    public void setTaxCryptoCode(String taxCryptoCode) {
        this.taxCryptoCode = taxCryptoCode.toUpperCase();
    }

    public void setProfit(String profit) {
        this.profit = profit.toUpperCase();
    }
}
