package com.example.financeWallet.dto;

import com.example.financeWallet.entity.BuyEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyDTO {
    private Long id;
    private String code; // code of the crypto bought
    private String codein; // name of the crypto used to buy
    private String  bid; // current price of crypto bought
    private String buyDate;
    private String amountCryptoPurchased;
    private String amountSpent; // amount used to buy crypto
    private String taxCryptoCode;
    private String  taxAmount;
    private String exchange;
    private String  profit;

    public BuyDTO(BuyEntity entity){
        BeanUtils.copyProperties(entity, this);
    }

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    public void setCodein(String codein){
        this.codein = codein.toUpperCase();
    }

    public void setTaxCryptoCode(String taxCryptoCode){
        this.taxCryptoCode = taxCryptoCode.toUpperCase();
    }

    public void setExchange(String exchange){
        this.exchange = exchange.toUpperCase();
    }
}
