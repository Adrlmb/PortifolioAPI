package com.example.financeWallet.dto;

import com.example.financeWallet.entity.BuyEntity;
import org.springframework.beans.BeanUtils;

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

    public BuyDTO(BuyEntity entity){
        BeanUtils.copyProperties(entity, this);
    }

    public BuyDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchasedCryptoCode() {
        return purchasedCryptoCode;
    }

    public void setPurchasedCryptoCode(String purchasedCryptoCode) {
        this.purchasedCryptoCode = purchasedCryptoCode;
    }

    public String getCryptoUsed() {
        return cryptoUsed;
    }

    public void setCryptoUsed(String cryptoUsed) {
        this.cryptoUsed = cryptoUsed;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getAmountCryptoPurchased() {
        return amountCryptoPurchased;
    }

    public void setAmountCryptoPurchased(String amountCryptoPurchased) {
        this.amountCryptoPurchased = amountCryptoPurchased;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getTaxCryptoCode() {
        return taxCryptoCode;
    }

    public void setTaxCryptoCode(String taxCryptoCode) {
        this.taxCryptoCode = taxCryptoCode;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "BuyDTO{" +
                "id=" + id +
                ", purchasedCryptoCode='" + purchasedCryptoCode + '\'' +
                ", cryptoUsed='" + cryptoUsed + '\'' +
                ", buyDate='" + buyDate + '\'' +
                ", amountCryptoPurchased='" + amountCryptoPurchased + '\'' +
                ", amountSpent='" + amountSpent + '\'' +
                ", taxCryptoCode='" + taxCryptoCode + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
